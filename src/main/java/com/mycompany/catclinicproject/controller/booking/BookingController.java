package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.dao.*;
import com.mycompany.catclinicproject.model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebServlet(name = "BookingController", urlPatterns = {"/Booking"})
public class BookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?from=booking");
            return;
        }
        loadBookingData(request, user);
        request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy các tham số bắt buộc
            String categoryIDStr = request.getParameter("categoryID");
            String serviceIDStr   = request.getParameter("serviceID");
            String catIDStr       = request.getParameter("catID");
            String note           = request.getParameter("note");

            if (categoryIDStr == null || serviceIDStr == null || catIDStr == null) {
                throw new Exception("Thiếu thông tin category, service hoặc mèo");
            }

            int categoryID = Integer.parseInt(categoryIDStr);
            int serviceID  = Integer.parseInt(serviceIDStr);
            int catID      = Integer.parseInt(catIDStr);

            // Khởi tạo DAO
            CategoryDAO categoryDAO = new CategoryDAO();
            ServiceDAO  serviceDAO  = new ServiceDAO();
            BookingDAO  bookingDAO  = new BookingDAO();
            UserDAO     userDAO     = new UserDAO();
            TimeSlotDAO slotDAO     = new TimeSlotDAO();

            // Lấy thông tin category & service
            Category category = categoryDAO.getCategoryById(categoryID);
            if (category == null) {
                throw new Exception("Không tìm thấy danh mục dịch vụ");
            }

            Service service = serviceDAO.getServiceById(serviceID);
            if (service == null || service.getCategoryID() != categoryID) {
                throw new Exception("Dịch vụ không tồn tại hoặc không thuộc danh mục đã chọn");
            }

            // Xác định loại dịch vụ dựa trên tên category (chuẩn hơn contains)
            String categoryNameLower = category.getCategoryName().trim().toLowerCase();
            boolean isBoarding = categoryNameLower.contains("boarding") || categoryNameLower.equals("lưu trú");
            boolean isSurgery  = categoryNameLower.contains("surgery")  || categoryNameLower.equals("phẫu thuật");
            boolean needsVet   = !isBoarding && !isSurgery;

            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(note != null ? note.trim() : null);

            List<Integer> serviceIDs = Collections.singletonList(serviceID);
            double totalAmount = 0.0;

            if (needsVet) {
                // Dịch vụ cần bác sĩ (Vaccination, Checkup, Test, ...)
                String assigneeStr = request.getParameter("assigneeInfo");
                String slotStr     = request.getParameter("slotID");

                if (assigneeStr == null || slotStr == null) {
                    throw new Exception("Thiếu thông tin bác sĩ hoặc khung giờ");
                }

                int assigneeUserID = Integer.parseInt(assigneeStr);
                int slotID         = Integer.parseInt(slotStr);

                Integer vetID = userDAO.getVetIDByUserID(assigneeUserID);
                if (vetID == null) {
                    throw new Exception("Người được chọn không phải bác sĩ thú y");
                }

                if (bookingDAO.isCatMedicalConflict(catID, slotID)) {
                    throw new Exception("Mèo đã có lịch khám trong khung giờ này");
                }

                TimeSlot slot = slotDAO.getSlotByID(slotID);
                if (slot == null) {
                    throw new Exception("Khung giờ không tồn tại");
                }

                booking.setVeterinarianID(vetID);
                booking.setStaffID(0);
                booking.setSlotID(slotID);
                booking.setAppointmentDate(slot.getSlotDate());
                booking.setEndDate(slot.getSlotDate());
                booking.setAppointmentTime(slot.getStartTime());

                totalAmount = service.getPrice();

            } else if (isBoarding) {
                // Dịch vụ lưu trú (Boarding)
                String startStr    = request.getParameter("startDate");
                String endStr      = request.getParameter("endDate");
                String checkInStr  = request.getParameter("checkInTime");

                if (startStr == null || endStr == null || checkInStr == null) {
                    throw new Exception("Thiếu thông tin ngày đến/đi hoặc giờ check-in");
                }

                LocalDate start   = LocalDate.parse(startStr);
                LocalDate end     = LocalDate.parse(endStr);
                LocalTime checkIn = LocalTime.parse(checkInStr);

                if (end.isBefore(start)) {
                    throw new Exception("Ngày trả phải sau ngày nhận");
                }

                if (bookingDAO.isCatHotelConflict(catID,
                        java.sql.Date.valueOf(start),
                        java.sql.Date.valueOf(end),
                        java.sql.Time.valueOf(checkIn))) {
                    throw new Exception("Mèo đã có lịch lưu trú trong khoảng thời gian này");
                }

                long days = ChronoUnit.DAYS.between(start, end) + 1;
                totalAmount = service.getPrice() * days;

                Integer staffID = userDAO.getRandomStaffByPosition("Care");
                if (staffID == null) {
                    throw new Exception("Hiện không có nhân viên chăm sóc khả dụng");
                }

                booking.setVeterinarianID(0);
                booking.setStaffID(staffID);
                booking.setSlotID(0);
                booking.setAppointmentDate(java.sql.Date.valueOf(start));
                booking.setEndDate(java.sql.Date.valueOf(end));
                booking.setAppointmentTime(java.sql.Time.valueOf(checkIn));

            } else if (isSurgery) {
                // Dịch vụ phẫu thuật
                String dateStr = request.getParameter("startDate");
                if (dateStr == null) {
                    throw new Exception("Thiếu ngày phẫu thuật");
                }

                LocalDate date = LocalDate.parse(dateStr);

                Integer staffID = userDAO.getRandomStaffByPosition("Technician");
                if (staffID == null) {
                    // Fallback nếu chưa có Technician → có thể dùng Assistant hoặc Nurse
                    staffID = userDAO.getRandomStaffByPosition("Assistant");
                    if (staffID == null) {
                        throw new Exception("Hiện không có kỹ thuật viên/phụ tá khả dụng cho phẫu thuật");
                    }
                }

                booking.setVeterinarianID(0);
                booking.setStaffID(staffID);
                booking.setSlotID(0);
                booking.setAppointmentDate(java.sql.Date.valueOf(date));
                booking.setEndDate(java.sql.Date.valueOf(date));
                booking.setAppointmentTime(java.sql.Time.valueOf("09:00:00")); // mặc định

                totalAmount = service.getPrice();

            } else {
                throw new Exception("Loại dịch vụ không được hỗ trợ");
            }

            List<Double> prices = Collections.singletonList(totalAmount);
            int bookingID = bookingDAO.createBookingWithInvoice(booking, serviceIDs, prices, totalAmount);

            response.sendRedirect(request.getContextPath() + "/vnpay?bookingID=" + bookingID);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu đầu vào không hợp lệ (số không đúng định dạng)");
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
        }
    }

    private void loadBookingData(HttpServletRequest request, User user) {
        CatDAO catDAO       = new CatDAO();
        ServiceDAO serviceDAO  = new ServiceDAO();
        UserDAO userDAO     = new UserDAO();
        TimeSlotDAO slotDAO    = new TimeSlotDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());

        request.setAttribute("catList", catDAO.getCatsByOwnerPaging(ownerID, 1, 10));
        request.setAttribute("categoryList", categoryDAO.getAllCategories());
        request.setAttribute("currentDate", LocalDate.now().toString());

        String categoryParam = request.getParameter("categoryID");

        if (categoryParam != null && !categoryParam.trim().isEmpty()) {
            try {
                int catID = Integer.parseInt(categoryParam);
                Category category = categoryDAO.getCategoryById(catID);

                if (category != null) {
                    String categoryNameLower = category.getCategoryName().trim().toLowerCase();
                    boolean isBoarding = categoryNameLower.contains("boarding") || categoryNameLower.equals("lưu trú");
                    boolean isSurgery  = categoryNameLower.contains("surgery")  || categoryNameLower.equals("phẫu thuật");
                    boolean needsVet   = !isBoarding && !isSurgery;

                    request.setAttribute("selectedCategoryID", catID);
                    request.setAttribute("serviceList", serviceDAO.getServicesByCategoryID(catID));
                    request.setAttribute("isBoarding", isBoarding);
                    request.setAttribute("isSurgery", isSurgery);
                    request.setAttribute("needsVet", needsVet);

                    if (needsVet) {
                        request.setAttribute("listPerson", userDAO.getAllVeterinarians());

                        String assigneeParam = request.getParameter("assigneeInfo");
                        String dateParam     = request.getParameter("startDate");

                        if (assigneeParam != null && !assigneeParam.isEmpty() &&
                                dateParam != null && !dateParam.isEmpty()) {
                            try {
                                int assigneeUserID = Integer.parseInt(assigneeParam);
                                Integer vetID = userDAO.getVetIDByUserID(assigneeUserID);
                                if (vetID != null) {
                                    request.setAttribute("slotListGrouped",
                                            slotDAO.getSlotsNext7DaysFromDate(vetID, dateParam));
                                }
                            } catch (NumberFormatException ignored) {
                                // Bỏ qua nếu param không hợp lệ → JSP sẽ không hiển thị slot
                            }
                        }
                    }
                }
            } catch (NumberFormatException ignored) {
                // Nếu categoryID không phải số → load mặc định
            }
        }

        // Nếu chưa chọn category → load tất cả services (hoặc để trống tùy UX)
        if (request.getAttribute("serviceList") == null) {
            request.setAttribute("serviceList", serviceDAO.getAllServices());
        }
    }
}