package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.*;
import com.mycompany.catclinicproject.model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebServlet(name = "CounterBookingController", urlPatterns = {"/reception/counter-booking"})
public class CounterBookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loadBookingData(request);
        request.getRequestDispatcher("/WEB-INF/views/reception/counter-booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        // Nếu chỉ là thao tác chọn Category/Service trên giao diện thì load lại form
        if (!"Confirm".equals(action)) {
            loadBookingData(request);
            request.getRequestDispatcher("/WEB-INF/views/reception/counter-booking.jsp").forward(request, response);
            return;
        }

        try {
            BookingDAO bookingDAO = new BookingDAO();
            CategoryDao categoryDAO = new CategoryDao();
            ServiceDAO serviceDAO = new ServiceDAO();
            UserDAO userDAO = new UserDAO();
            TimeSlotDAO slotDAO = new TimeSlotDAO();

            String phone = request.getParameter("phone");
            User customer = userDAO.getUserByPhone(phone);

            int catID = 0;
            String catIDStr = request.getParameter("catID");
            if (catIDStr != null && !catIDStr.isEmpty()) {
                catID = Integer.parseInt(catIDStr);
            }

            // TỰ ĐỘNG TẠO KHÁCH VÀ MÈO NẾU LÀ KHÁCH VÃNG LAI MỚI (CHƯA CÓ TRONG DB)
            if (customer == null || catID == 0) {
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String petName = request.getParameter("petName");
                String breed = request.getParameter("breed");
                String ageStr = request.getParameter("age");
                String gender = request.getParameter("gender");

                if (petName == null || petName.trim().isEmpty()) {
                    throw new Exception("Vui lòng nhập tên thú cưng cho khách mới!");
                }
                int age = (ageStr != null && !ageStr.isEmpty()) ? Integer.parseInt(ageStr) : 1;

                ReceptionDAO receptionDAO = new ReceptionDAO();
                catID = receptionDAO.createWalkInCustomerAndPet(phone, fullName, email, petName, breed, age, gender);

                if (catID <= 0) throw new Exception("Lỗi hệ thống khi tạo hồ sơ khách hàng.");
            }

            // Lấy thông tin dịch vụ
            String categoryIDStr = request.getParameter("categoryID");
            String serviceIDStr = request.getParameter("serviceID");
            if (categoryIDStr == null || categoryIDStr.isEmpty()) throw new Exception("Vui lòng chọn loại dịch vụ.");
            if (serviceIDStr == null || serviceIDStr.isEmpty()) throw new Exception("Vui lòng chọn dịch vụ cụ thể.");

            int categoryID = Integer.parseInt(categoryIDStr);
            int serviceID = Integer.parseInt(serviceIDStr);

            Category category = categoryDAO.getCategoryById(categoryID);
            Service service = serviceDAO.getServiceById(serviceID);
            if (category == null || service == null) throw new Exception("Dịch vụ không hợp lệ.");

            String categoryName = category.getCategoryName().toLowerCase();
            boolean isBoarding = categoryName.contains("boarding");
            boolean isCheckup = categoryName.contains("checkup");
            boolean needsVet = !isBoarding && !isCheckup;

            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(request.getParameter("note"));
            booking.setStatus("PendingPayment");
            booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));

            double totalAmount = 0;

            // Xử lý luồng Khám (Cần Bác sĩ)
            if (needsVet) {
                String slotIDStr = request.getParameter("slotID");
                String slotDateStr = request.getParameter("slotDate");
                String vetUserIDStr = request.getParameter("assigneeInfo");

                if (slotIDStr == null || slotDateStr == null || vetUserIDStr == null || vetUserIDStr.isEmpty()) {
                    throw new Exception("Vui lòng chọn bác sĩ, ngày và khung giờ khám.");
                }

                int slotID = Integer.parseInt(slotIDStr);
                int vetUserID = Integer.parseInt(vetUserIDStr);
                Integer realVetID = userDAO.getVetIDByUserId(vetUserID);
                if (realVetID == null) throw new Exception("Bác sĩ không hợp lệ.");

                TimeSlot slot = slotDAO.getSlotByID(slotID);
                java.sql.Date appointmentDate = java.sql.Date.valueOf(slotDateStr);

                if (bookingDAO.isCatBusyAtSlot(catID, appointmentDate, slotID)) {
                    throw new Exception("Mèo đã có lịch hẹn khác vào khung giờ này.");
                }

                booking.setVeterinarianID(realVetID);
                booking.setSlotID(slotID);
                booking.setAppointmentDate(appointmentDate);
                booking.setEndDate(appointmentDate);
                booking.setAppointmentTime(slot.getStartTime());
                totalAmount = service.getPrice();

            }
            // Xử lý luồng Kỹ thuật (Checkup)
            else if (isCheckup) {
                String startDateStr = request.getParameter("startDate");
                String timeStr = request.getParameter("checkInTime");
                String staffIDStr = request.getParameter("StaffInfor");
                if (startDateStr == null || timeStr == null || staffIDStr == null) throw new Exception("Vui lòng chọn ngày, giờ và nhân viên kỹ thuật.");

                java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
                java.sql.Time checkInTime = java.sql.Time.valueOf(timeStr + ":00");

                if (bookingDAO.isCatBusyAtTime(catID, startDate, checkInTime)) throw new Exception("Mèo đã bận lúc này.");

                booking.setAppointmentDate(startDate);
                booking.setEndDate(startDate);
                booking.setAppointmentTime(checkInTime);
                booking.setStaffID(Integer.parseInt(staffIDStr));
                totalAmount = service.getPrice();

            }
            // Xử lý luồng Lưu trú (Boarding)
            else if (isBoarding) {
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String timeStr = request.getParameter("checkInTime");
                String staffIDStr = request.getParameter("StaffInfor");

                if (startDateStr == null || endDateStr == null || timeStr == null || staffIDStr == null) throw new Exception("Vui lòng điền đầy đủ thông tin lưu trú.");

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

            // Gọi DAO tạo hóa đơn và lịch
            List<Integer> serviceList = Collections.singletonList(serviceID);
            List<Double> priceList = Collections.singletonList(totalAmount);

            int bookingID = bookingDAO.createBookingWithInvoice(booking, serviceList, priceList, totalAmount);

            if (bookingID > 0) {
                bookingDAO.confirmBooking(bookingID); // Tự Confirm vì Lễ tân đặt
                request.setAttribute("bookingID", bookingID);
                request.getRequestDispatcher("/WEB-INF/views/reception/BookingSuccess.jsp").forward(request, response);
                return;
            }

            if (bookingID == -2) throw new Exception("Khung giờ này vừa có người khác đặt mất rồi.");
            throw new Exception("Lỗi hệ thống khi tạo lịch hẹn (Mã lỗi: " + bookingID + ").");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            loadBookingData(request);
            request.getRequestDispatcher("/WEB-INF/views/reception/counter-booking.jsp").forward(request, response);
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
            User existingCustomer = userDAO.getUserByPhone(phone);
            if (existingCustomer != null) {
                request.setAttribute("customerInfo", existingCustomer);
                int ownerID = catDAO.getOwnerIdByUserId(existingCustomer.getUserID());
                request.setAttribute("catList", catDAO.getCatsByOwnerID(ownerID));
            }
        }

        request.setAttribute("categoryList", categoryDAO.getAllCategories());
        request.setAttribute("currentDate", LocalDate.now().toString());

        String categoryIDStr = request.getParameter("categoryID");
        if (categoryIDStr != null && !categoryIDStr.isEmpty()) {
            int categoryID = Integer.parseInt(categoryIDStr);
            Category category = categoryDAO.getCategoryById(categoryID);
            request.setAttribute("selectedCategoryID", categoryID);

            if (category != null) {
                String name = category.getCategoryName().toLowerCase();
                boolean isBoarding = name.contains("boarding");
                boolean isCheckup = name.contains("checkup");
                boolean needsVet = !isBoarding && !isCheckup;

                request.setAttribute("isBoarding", isBoarding);
                request.setAttribute("isCheckup", isCheckup);
                request.setAttribute("needsVet", needsVet);
                request.setAttribute("serviceList", serviceDAO.getServicesByCategoryID(categoryID));

                if (isBoarding) request.setAttribute("listStaff", userDAO.getAllStaffByPosition("Care"));
                else if (isCheckup) request.setAttribute("listStaff", userDAO.getAllStaffByPosition("Technician"));

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

        request.setAttribute("selectedCatID", request.getParameter("catID"));
        request.setAttribute("selectedServiceID", request.getParameter("serviceID"));
        request.setAttribute("selectedStartDate", request.getParameter("startDate"));
        request.setAttribute("selectedEndDate", request.getParameter("endDate"));
        request.setAttribute("selectedSlotID", request.getParameter("slotID"));
    }
}