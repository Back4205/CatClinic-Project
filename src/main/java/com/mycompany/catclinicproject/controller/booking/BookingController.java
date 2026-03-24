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

        if (user == null || user.getRoleID() != 5) {
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

        if (!"pay".equals(action)) {
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
            return;
        }

        try {

            BookingDAO bookingDAO = new BookingDAO();
            CategoryDao categoryDAO = new CategoryDao();
            ServiceDAO serviceDAO = new ServiceDAO();
            UserDAO userDAO = new UserDAO();
            TimeSlotDAO slotDAO = new TimeSlotDAO();

            int catID = Integer.parseInt(request.getParameter("catID"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            int serviceID = Integer.parseInt(request.getParameter("serviceID"));

            String note = request.getParameter("note");

            Category category = categoryDAO.getCategoryById(categoryID);
            Service service = serviceDAO.getServiceById(serviceID);

            if (category == null || service == null) {
                throw new Exception("The service does not exist.");
            }

            String categoryName = category.getCategoryName().toLowerCase();
            int     categoryIDtmp  =  category.getCategoryID();
            boolean isBoarding = categoryName.contains("boarding") || (categoryIDtmp == 4);
            boolean isParaclinical = categoryName.contains("paraclinical")|| (categoryIDtmp == 5);
            boolean needsVet = !isBoarding && !isParaclinical;

            Booking booking = new Booking();

            booking.setCatID(catID);
            booking.setNote(note);
            booking.setStatus("PendingPayment");
            booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));

            double totalAmount = 0;
            if (needsVet) {

                int slotID = Integer.parseInt(request.getParameter("slotID"));
                int vetID = Integer.parseInt(request.getParameter("vetID"));

                java.sql.Date appointmentDate =
                        java.sql.Date.valueOf(request.getParameter("slotDate"));

                TimeSlot slot = slotDAO.getSlotByID(slotID);

                if (slot == null) {
                    throw new Exception("Invalid time slot.");
                }

                if (bookingDAO.isCatBusyAtSlot(catID, appointmentDate, slotID)) {
                    throw new Exception("The cat already has a schedule for this time slot.");
                }

                booking.setVeterinarianID(vetID);
                booking.setSlotID(slotID);
                booking.setAppointmentDate(appointmentDate);
                booking.setEndDate(appointmentDate);
                booking.setAppointmentTime(slot.getStartTime());

                totalAmount = service.getPrice();
            }
            else if (isParaclinical) {

                java.sql.Date date =
                        java.sql.Date.valueOf(request.getParameter("startDate"));

                java.sql.Time time = java.sql.Time.valueOf(
                        request.getParameter("checkInTime") + ":00"
                );

                if (bookingDAO.isCatBusyAtTime(catID, date, time )) {
                    throw new Exception("The cat already has a schedule for this time slot.");
                }

                booking.setAppointmentDate(date);
                booking.setEndDate(date);
                booking.setAppointmentTime(time);

                Integer staffID = userDAO.getRandomStaffByPosition("Technician");

                if (staffID == null) {
                    throw new Exception("No technicians are available.");
                }

                booking.setStaffID(staffID);

                totalAmount = service.getPrice();
            }

            else if (isBoarding) {

                LocalDate start = LocalDate.parse(request.getParameter("startDate"));
                LocalDate end = LocalDate.parse(request.getParameter("endDate"));

                if (end.isBefore(start)) {
                    throw new Exception("The end date must be after the start date.");
                }

                if (bookingDAO.isCatHotelConflict(
                        catID,
                        java.sql.Date.valueOf(start),
                        java.sql.Date.valueOf(end))) {

                    throw new Exception("The length of stay overlaps.");
                }

                long days = ChronoUnit.DAYS.between(start, end) + 1;

                totalAmount = service.getPrice() * days;

                booking.setAppointmentDate(java.sql.Date.valueOf(start));
                booking.setEndDate(java.sql.Date.valueOf(end));

                booking.setAppointmentTime(
                        java.sql.Time.valueOf(request.getParameter("checkInTime") + ":00")
                );

                Integer staffID = userDAO.getRandomStaffByPosition("Care");

                if (staffID == null) {
                    throw new Exception("There is no caregiver present.");
                }

                booking.setStaffID(staffID);
            }

            List<Integer> serviceList = Collections.singletonList(serviceID);
            List<Double> priceList = Collections.singletonList(totalAmount);

            int bookingID =
                    bookingDAO.createBookingWithInvoice(
                            booking,
                            serviceList,
                            priceList,
                            totalAmount
                    );

            if (bookingID > 0) {

                response.sendRedirect(
                        request.getContextPath() + "/vnpay?bookingID=" + bookingID
                );

                return;
            }

            if (bookingID == -2)
                throw new Exception("The time slot has been booked.");

            if (bookingID == -1)
                throw new Exception("Unable to create a booking.");

            if (bookingID == -4)
                throw new Exception("System error.");

            throw new Exception("An unknown error.");

        }

        catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("error", e.getMessage());

            loadBookingData(request, user);

            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp")
                    .forward(request, response);
        }
    }

    private void loadBookingData(HttpServletRequest request, User user) {
        CatDAO catDAO = new CatDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        UserDAO userDAO = new UserDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        CategoryDao categoryDAO = new CategoryDao();
        String catIDStr = request.getParameter("catID");
        String serviceIDStr = request.getParameter("serviceID");
        String categoryIDStr = request.getParameter("categoryID");

        if (catIDStr != null && !catIDStr.isEmpty()) {
            request.setAttribute("selectedCatID", Integer.parseInt(catIDStr));
        }

        if (serviceIDStr != null && !serviceIDStr.isEmpty()) {
            request.setAttribute("selectedServiceID", Integer.parseInt(serviceIDStr));
        }

        if (categoryIDStr != null && !categoryIDStr.isEmpty()) {
            request.setAttribute("selectedCategoryID", Integer.parseInt(categoryIDStr));
        }

        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());
        int page = 1;
        int pageSize = 5;

        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception e) {
                page = 1;
            }
        }
        List<Cat> list = catDAO.getCatsPagingByOwner(ownerID, pageSize, page);
        int totalItems = catDAO.countCatsByOwner(ownerID);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        request.setAttribute("catList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categoryList", categoryDAO.getAllCategories());
        request.setAttribute("currentDate", LocalDate.now().toString());


        if (categoryIDStr != null) {
            int categoryID = Integer.parseInt(categoryIDStr);
            Category category = categoryDAO.getCategoryById(categoryID);
            request.setAttribute("selectedCategoryID", categoryID);
            int     categoryIDtmp  =  category.getCategoryID();
            if (category != null) {
                String name = category.getCategoryName().toLowerCase();
                boolean isBoarding = name.contains("boarding") || (categoryIDtmp == 4);
                boolean isParaclinical = name.contains("paraclinical")|| (categoryIDtmp == 5);
                boolean needsVet = !isBoarding && !isParaclinical;

                request.setAttribute("isBoarding", isBoarding);
                request.setAttribute("isParaclinical", isParaclinical);
                request.setAttribute("needsVet", needsVet);
                request.setAttribute("serviceList", serviceDAO.getServicesByCategoryID(categoryID));

                if (needsVet) {
                    request.setAttribute("listPerson", userDAO.getAllVeterinarians());
                    String vetUserIDStr = request.getParameter("assigneeInfo");
                    String startDateStr = request.getParameter("startDate");

                    if (vetUserIDStr != null && !vetUserIDStr.isEmpty() && startDateStr != null && !startDateStr.isEmpty()) {
                        int vetUserID = Integer.parseInt(vetUserIDStr);
                        Integer vetID = userDAO.getVetIDByUserId(vetUserID);
                        if (vetID != null) {
                            java.sql.Date fromDate = java.sql.Date.valueOf(startDateStr);
                            request.setAttribute("slotList", slotDAO.getAvailableSlotsNext7Days(vetID, fromDate));
                        }
                    }
                }
            }
        }
        String noteParam = request.getParameter("note");
        if (noteParam != null) {
            request.setAttribute("savedNote", noteParam);
        }

        String startDateParam = request.getParameter("startDate");
        if (startDateParam != null && !startDateParam.isEmpty()) {
            request.setAttribute("selectedStartDate", startDateParam);
        }

        String endDateParam = request.getParameter("endDate");
        if (endDateParam != null && !endDateParam.isEmpty()) {
            request.setAttribute("selectedEndDate", endDateParam);
        }
    }
}