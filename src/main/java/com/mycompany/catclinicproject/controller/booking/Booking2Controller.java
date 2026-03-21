package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.dao.*;
import com.mycompany.catclinicproject.model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebServlet(name = "Booking2Controller", urlPatterns = {"/Booking2"})
public class Booking2Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadBookingData(request);
        request.getRequestDispatcher("/WEB-INF/views/reception/Booking2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        // Nếu không phải action xác nhận, chỉ load lại dữ liệu (ví dụ khi chọn Category/Service)
        if (!"Confirm".equals(action)) {
            loadBookingData(request);
            request.getRequestDispatcher("/WEB-INF/views/reception/Booking2.jsp").forward(request, response);
            return;
        }

        try {
            BookingDAO bookingDAO = new BookingDAO();
            CategoryDao categoryDAO = new CategoryDao();
            ServiceDAO serviceDAO = new ServiceDAO();
            UserDAO userDAO = new UserDAO();
            TimeSlotDAO slotDAO = new TimeSlotDAO();

            // 1. Kiểm tra thông tin khách hàng qua số điện thoại
            String phone = request.getParameter("phone");
            Owner customer = userDAO.getUserByPhone(phone);
            if (customer == null) {
                throw new Exception("Không tìm thấy khách hàng! Vui lòng tạo hồ sơ khách hàng trước.");
            }

            // 2. Lấy các ID cơ bản
            String catIDStr = request.getParameter("catID");
            String categoryIDStr = request.getParameter("categoryID");
            String serviceIDStr = request.getParameter("serviceID");

            if (catIDStr == null || catIDStr.isEmpty()) throw new Exception("Vui lòng chọn thú cưng.");
            if (categoryIDStr == null || categoryIDStr.isEmpty()) throw new Exception("Vui lòng chọn loại dịch vụ.");
            if (serviceIDStr == null || serviceIDStr.isEmpty()) throw new Exception("Vui lòng chọn dịch vụ cụ thể.");

            int catID = Integer.parseInt(catIDStr);
            int categoryID = Integer.parseInt(categoryIDStr);
            int serviceID = Integer.parseInt(serviceIDStr);

            Category category = categoryDAO.getCategoryById(categoryID);
            Service service = serviceDAO.getServiceById(serviceID);
            if (category == null || service == null) throw new Exception("Dịch vụ không hợp lệ.");

            String categoryName = category.getCategoryName().toLowerCase();
            int     categoryIDtmp  =  category.getCategoryID();
            boolean isBoarding = categoryName.contains("boarding") || (categoryIDtmp == 4);
            boolean isParaclinical = categoryName.contains("paraclinical")|| (categoryIDtmp == 5);
            boolean needsVet = !isBoarding && !isParaclinical;

            // 4. Khởi tạo đối tượng Booking
            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(request.getParameter("note"));
            booking.setStatus("PendingPayment");
            booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));

            double totalAmount = 0;

            // --- TRƯỜNG HỢP: KHÁM BỆNH (CẦN BÁC SĨ & SLOT) ---
            if (needsVet) {
                String slotIDStr = request.getParameter("slotID");
                String slotDateStr = request.getParameter("slotDate");
                String vetUserIDStr = request.getParameter("assigneeInfo"); // UserID của bác sĩ

                if (slotIDStr == null || slotDateStr == null || vetUserIDStr == null || vetUserIDStr.isEmpty()) {
                    throw new Exception("Vui lòng chọn bác sĩ, ngày và khung giờ khám.");
                }

                int slotID = Integer.parseInt(slotIDStr);
                int vetUserID = Integer.parseInt(vetUserIDStr);

                // QUAN TRỌNG: Chuyển UserID -> VetID để khớp bảng TimeSlot_Vet
                Integer realVetID = userDAO.getVetIDByUserId(vetUserID);
                if (realVetID == null) throw new Exception("Bác sĩ không hợp lệ.");

                TimeSlot slot = slotDAO.getSlotByID(slotID);
                java.sql.Date appointmentDate = java.sql.Date.valueOf(slotDateStr);

                if (bookingDAO.isCatBusyAtSlot(catID, appointmentDate, slotID)) {
                    throw new Exception("Mèo đã có lịch hẹn khác vào khung giờ này.");
                }

                booking.setVeterinarianID(realVetID); // Dùng RealVetID cho DAO
                booking.setSlotID(slotID);
                booking.setAppointmentDate(appointmentDate);
                booking.setEndDate(appointmentDate);
                booking.setAppointmentTime(slot.getStartTime());
                totalAmount = service.getPrice();
            }

            // --- TRƯỜNG HỢP: KIỂM TRA ĐỊNH KỲ (CHECKUP) ---
            else if (isParaclinical) {
                String startDateStr = request.getParameter("startDate");
                String timeStr = request.getParameter("checkInTime");
                String staffIDStr = request.getParameter("StaffInfor");

                if (startDateStr == null || timeStr == null || staffIDStr == null) {
                    throw new Exception("Vui lòng chọn ngày, giờ và nhân viên kỹ thuật.");
                }

                java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
                java.sql.Time checkInTime = java.sql.Time.valueOf(timeStr + ":00");

                if (bookingDAO.isCatBusyAtTime(catID, startDate, checkInTime)) {
                    throw new Exception("Mèo đã bận lúc này.");
                }

                booking.setAppointmentDate(startDate);
                booking.setEndDate(startDate);
                booking.setAppointmentTime(checkInTime);
                booking.setStaffID(Integer.parseInt(staffIDStr));
                totalAmount = service.getPrice();
            }

            // --- TRƯỜNG HỢP: LƯU TRÚ (BOARDING) ---
            else if (isBoarding) {
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String timeStr = request.getParameter("checkInTime");
                String staffIDStr = request.getParameter("StaffInfor");

                if (startDateStr == null || endDateStr == null || timeStr == null || staffIDStr == null) {
                    throw new Exception("Vui lòng điền đầy đủ thông tin lưu trú.");
                }

                LocalDate start = LocalDate.parse(startDateStr);
                LocalDate end = LocalDate.parse(endDateStr);

                if (end.isBefore(start)) throw new Exception("Ngày kết thúc không hợp lệ.");
                if (bookingDAO.isCatHotelConflict(catID, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end))) {
                    throw new Exception("Trùng lịch lưu trú.");
                }

                long days = ChronoUnit.DAYS.between(start, end) + 1;
                totalAmount = service.getPrice() * days;

                booking.setAppointmentDate(java.sql.Date.valueOf(start));
                booking.setEndDate(java.sql.Date.valueOf(end));
                booking.setAppointmentTime(java.sql.Time.valueOf(timeStr + ":00"));
                booking.setStaffID(Integer.parseInt(staffIDStr));
            }

            // 5. Thực hiện lưu vào Database (Gồm cả Lock Slot trong DAO)
            List<Integer> serviceList = Collections.singletonList(serviceID);
            List<Double> priceList = Collections.singletonList(totalAmount);

            int bookingID = bookingDAO.createBookingWithInvoice(booking, serviceList, priceList, totalAmount);

            if (bookingID > 0) {
                // Đối với nhân viên đặt hộ, mặc định chuyển trạng thái sang Confirmed ngay
                bookingDAO.confirmBooking(bookingID);

                request.setAttribute("bookingID", bookingID);
                request.getRequestDispatcher("/WEB-INF/views/reception/BookingSuccess.jsp").forward(request, response);
                return;
            }

            // Xử lý các mã lỗi từ DAO
            if (bookingID == -2) throw new Exception("Thật xin lỗi, khung giờ này vừa có người khác đặt mất rồi.");
            throw new Exception("Lỗi hệ thống khi tạo lịch hẹn (Mã lỗi: " + bookingID + ").");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            loadBookingData(request);
            request.getRequestDispatcher("/WEB-INF/views/reception/Booking2.jsp").forward(request, response);
        }
    }

    private void loadBookingData(HttpServletRequest request) {
        CatDAO catDAO = new CatDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        UserDAO userDAO = new UserDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        CategoryDao categoryDAO = new CategoryDao();

        String phone = request.getParameter("phone");
        if (phone != null && !phone.trim().isEmpty()) {
            Owner existingCustomer = userDAO.getUserByPhone(phone);
            if (existingCustomer != null) {
                request.setAttribute("customerInfo", existingCustomer);
                int ownerID = catDAO.getOwnerIdByUserId(existingCustomer.getUserID());
                request.setAttribute("catList", catDAO.getCatsByOwnerID(ownerID));
            }
        }

        request.setAttribute("categoryList", categoryDAO.getAllCategories());
        request.setAttribute("currentDate", LocalDate.now().toString());

        // Logic load Service và Slots dựa trên lựa chọn hiện tại
        String categoryIDStr = request.getParameter("categoryID");
        if (categoryIDStr != null && !categoryIDStr.isEmpty()) {
            int categoryID = Integer.parseInt(categoryIDStr);
            Category category = categoryDAO.getCategoryById(categoryID);
            request.setAttribute("selectedCategoryID", categoryID);

            if (category != null) {
                String name = category.getCategoryName().toLowerCase();
                boolean isBoarding = name.contains("boarding");
                boolean isParaclinical = name.contains("paraclinical");
                boolean needsVet = !isBoarding && !isParaclinical;

                request.setAttribute("isBoarding", isBoarding);
                request.setAttribute("isParaclinical", isParaclinical);
                request.setAttribute("needsVet", needsVet);
                request.setAttribute("serviceList", serviceDAO.getServicesByCategoryID(categoryID));

                // Load nhân viên phù hợp
                if (isBoarding) request.setAttribute("listStaff", userDAO.getAllStaffByPosition("Care"));
                else if (isParaclinical) request.setAttribute("listStaff", userDAO.getAllStaffByPosition("Technician"));

                // Load bác sĩ và slots
                if (needsVet) {
                    request.setAttribute("listPerson", userDAO.getAllVeterinarians());
                    String vetUserIDStr = request.getParameter("assigneeInfo");
                    String startDateStr = request.getParameter("startDate");
                    if (vetUserIDStr != null && !vetUserIDStr.isEmpty() && startDateStr != null && !startDateStr.isEmpty()) {
                        Integer vetID = userDAO.getVetIDByUserId(Integer.parseInt(vetUserIDStr));
                        if (vetID != null) {
                            request.setAttribute("slotList", slotDAO.getAvailableSlotsNext7Days(vetID, java.sql.Date.valueOf(startDateStr)));
                        }
                    }
                }
            }
        }

        // Giữ lại các giá trị đã chọn để UI không bị reset
        request.setAttribute("selectedCatID", request.getParameter("catID"));
        request.setAttribute("selectedServiceID", request.getParameter("serviceID"));
        request.setAttribute("selectedStartDate", request.getParameter("startDate"));
        request.setAttribute("selectedEndDate", request.getParameter("endDate"));
        request.setAttribute("selectedSlotID", request.getParameter("slotID"));
    }
}