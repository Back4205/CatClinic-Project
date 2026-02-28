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

        String action = request.getParameter("action");

        // TRƯỜNG HỢP 1: Load lại dữ liệu khi đổi Category/Service/Date
        if (action == null || action.isEmpty()) {
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
            return;
        }

        // TRƯỜNG HỢP 2: Thực hiện đặt lịch (Book hoặc Pay)
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            int serviceID  = Integer.parseInt(request.getParameter("serviceID"));
            int catID      = Integer.parseInt(request.getParameter("catID"));

            BookingDAO  bookingDAO  = new BookingDAO();
            CategoryDao categoryDAO = new CategoryDao();
            ServiceDAO  serviceDAO  = new ServiceDAO();
            UserDAO     userDAO     = new UserDAO();
            TimeSlotDAO slotDAO     = new TimeSlotDAO();

            Category category = categoryDAO.getCategoryById(categoryID);
            Service service = serviceDAO.getServiceById(serviceID);
            String catName = category.getCategoryName().toLowerCase();
            boolean isBoarding = catName.contains("boarding") || catName.contains("lưu trú");
            boolean needsVet   = !isBoarding ;

            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(request.getParameter("note"));

            double totalAmount = 0.0;
            if (needsVet) {
                String slotID = request.getParameter("slotID");
                String vetUserID = request.getParameter("assigneeInfo");
                if (slotID == null || vetUserID == null) throw new Exception("Vui lòng chọn bác sĩ và giờ khám.");
                int slotIdInt = Integer.parseInt(slotID);

                // 🔥 THÊM ĐOẠN NÀY
                if (bookingDAO.isCatMedicalConflict(catID, slotIdInt)) {
                    throw new Exception("Mèo đã có lịch khám khác trùng thời gian!");
                }

                TimeSlot slot = slotDAO.getSlotByID(Integer.parseInt(slotID));
                Integer vetID = userDAO.getVetIDByUserID(Integer.parseInt(vetUserID));

                booking.setVeterinarianID(vetID);
                booking.setSlotID(slot.getSlotID());
                booking.setAppointmentDate(slot.getSlotDate());
                booking.setAppointmentTime(slot.getStartTime());
                booking.setEndDate(slot.getSlotDate());
                totalAmount = service.getPrice();
            } else if (isBoarding) {
                LocalDate start = LocalDate.parse(request.getParameter("startDate"));
                LocalDate end = LocalDate.parse(request.getParameter("endDate"));
                if (end.isBefore(start)) throw new Exception("Ngày kết thúc không hợp lệ.");

                long days = ChronoUnit.DAYS.between(start, end) + 1;
                totalAmount = service.getPrice() * days;

                booking.setAppointmentDate(java.sql.Date.valueOf(start));
                booking.setEndDate(java.sql.Date.valueOf(end));
                booking.setAppointmentTime(java.sql.Time.valueOf(request.getParameter("checkInTime") + ":00"));
                booking.setStaffID(userDAO.getRandomStaffByPosition("Care"));
            }

            // Lưu vào Database và nhận lại BookingID
            int bookingID = bookingDAO.createBookingWithInvoice(booking, Collections.singletonList(serviceID), Collections.singletonList(totalAmount), totalAmount);

            if ("pay".equals(action)) {
                response.sendRedirect(request.getContextPath() + "/vnpay?bookingID=" + bookingID);
            } else {
                response.sendRedirect(request.getContextPath() + "/booking-success?id=" + bookingID);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
        }
    }

    private void loadBookingData(HttpServletRequest request, User user) {
        CatDAO catDAO = new CatDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        UserDAO userDAO = new UserDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        CategoryDao categoryDAO = new CategoryDao();

        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());
        request.setAttribute("catList", catDAO.getCatsByOwnerID(ownerID));
        request.setAttribute("categoryList", categoryDAO.getAllCategories());
        request.setAttribute("currentDate", LocalDate.now().toString());

        // Giữ lại state đã chọn
        request.setAttribute("selectedCatID", request.getParameter("catID"));
        request.setAttribute("selectedCategoryID", request.getParameter("categoryID"));
        request.setAttribute("selectedServiceID", request.getParameter("serviceID"));

        String cIDStr = request.getParameter("categoryID");
        if (cIDStr != null && !cIDStr.isEmpty()) {
            int cID = Integer.parseInt(cIDStr);
            Category cat = categoryDAO.getCategoryById(cID);
            String name = cat.getCategoryName().toLowerCase();
            boolean isBoarding = name.contains("boarding");
            boolean needsVet = !isBoarding ;

            request.setAttribute("isBoarding", isBoarding);
            request.setAttribute("needsVet", needsVet);
            request.setAttribute("serviceList", serviceDAO.getServicesByCategoryID(cID));

            if (needsVet) {
                request.setAttribute("listPerson", userDAO.getAllVeterinarians());
                String vetUser = request.getParameter("assigneeInfo");
                String date = request.getParameter("startDate");
                if (vetUser != null && date != null) {
                    Integer vID = userDAO.getVetIDByUserID(Integer.parseInt(vetUser));
                    request.setAttribute("slotList", slotDAO.getSlotsNext7DaysFromDate(vID, date));
                }
            }
        }
    }
}