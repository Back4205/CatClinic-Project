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
        Integer staffId = null ;
        //  Nếu không phải pay thì chỉ reload form (do onchange submit)
        if (!"pay".equals(action)) {
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp").forward(request, response);
            return;
        }

        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            int serviceID  = Integer.parseInt(request.getParameter("serviceID"));
            int catID      = Integer.parseInt(request.getParameter("catID"));

            BookingDAO bookingDAO = new BookingDAO();
            CategoryDao categoryDAO = new CategoryDao();
            ServiceDAO serviceDAO = new ServiceDAO();
            UserDAO userDAO = new UserDAO();
            TimeSlotDAO slotDAO = new TimeSlotDAO();

            Category category = categoryDAO.getCategoryById(categoryID);
            Service service   = serviceDAO.getServiceById(serviceID);

            String categoryName = category.getCategoryName().toLowerCase();

            boolean isBoarding = categoryName.contains("boarding");
            boolean isCheckup  = categoryName.contains("checkup");
            boolean needsVet   = !isBoarding && !isCheckup;

            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(request.getParameter("note"));

            double totalAmount = 0.0;

            if (needsVet) {

                String slotID = request.getParameter("slotID");
                String vetUserID = request.getParameter("assigneeInfo");

                if (slotID == null || vetUserID == null)
                    throw new Exception("Vui lòng chọn bác sĩ và giờ khám.");
                int slotId = Integer.parseInt(slotID);

                // check trufng lich
                if (bookingDAO.isCatBusyAtSlot(catID, slotId)) {
                    throw new Exception("Mèo này đã có lịch hẹn với bác sĩ khác vào khung giờ này rồi!");
                }

                TimeSlot slot = slotDAO.getSlotByID(Integer.parseInt(slotID));
                Integer vetID = userDAO.getVetIDByUserID(Integer.parseInt(vetUserID));

                booking.setVeterinarianID(vetID);
                booking.setSlotID(slot.getSlotID());
                booking.setAppointmentDate(slot.getSlotDate());
                booking.setAppointmentTime(slot.getStartTime());
                booking.setEndDate(slot.getSlotDate());

                totalAmount = service.getPrice();
            }
            else if (isCheckup) {

                String date = request.getParameter("startDate");
                String time = request.getParameter("checkInTime");

                if (date == null || time == null || time.isEmpty())
                    throw new Exception("Vui lòng chọn ngày và giờ.");

                booking.setAppointmentDate(java.sql.Date.valueOf(date));
                booking.setEndDate(java.sql.Date.valueOf(date));
                booking.setAppointmentTime(java.sql.Time.valueOf(time + ":00"));

                staffId = userDAO.getRandomStaffByPosition("Technician");
                if (staffId == null){
                    throw new Exception("Không có Technician khả dụng.");
                }
                booking.setStaffID(staffId);

                totalAmount = service.getPrice();
            }


            else {

                LocalDate start = LocalDate.parse(request.getParameter("startDate"));
                LocalDate end   = LocalDate.parse(request.getParameter("endDate"));

                if (end.isBefore(start))
                    throw new Exception("Ngày kết thúc không hợp lệ.");

                if (bookingDAO.isCatHotelConflict(catID, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end))) {
                    throw new Exception("Mèo đã có lịch lưu trú trùng ngày!");
                }

                long days = ChronoUnit.DAYS.between(start, end) + 1;
                totalAmount = service.getPrice() * days;

                booking.setAppointmentDate(java.sql.Date.valueOf(start));
                booking.setEndDate(java.sql.Date.valueOf(end));
                booking.setAppointmentTime(java.sql.Time.valueOf(request.getParameter("checkInTime") + ":00"));

                staffId = userDAO.getRandomStaffByPosition("Care");
                if (staffId == null){
                    throw new Exception("Không có Care Staff khả dụng.");
                }


                booking.setStaffID(staffId);
            }


            List<Integer> serviceList = new ArrayList<>();
            serviceList.add(serviceID);

            List<Double> priceList = new ArrayList<>();
            priceList.add(totalAmount);

            int bookingID = bookingDAO.createBookingWithInvoice(booking, serviceList, priceList, totalAmount);

            if (bookingID < 0) {
                throw new Exception("Có lỗi khi tạo booking.");
            }
            response.sendRedirect(request.getContextPath() + "/vnpay?bookingID=" + bookingID);

        } catch (Exception e) {

            request.setAttribute("error", "Lỗi: " + e.getMessage());
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

        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());


        request.setAttribute("catList", catDAO.getCatsByOwnerID(ownerID));
        request.setAttribute("categoryList", categoryDAO.getAllCategories());
        request.setAttribute("currentDate", LocalDate.now().toString());

        String cIDStr = request.getParameter("categoryID");

        if (cIDStr != null && !cIDStr.isEmpty()) {

            int cID = Integer.parseInt(cIDStr);
            Category category = categoryDAO.getCategoryById(cID);
            request.setAttribute("selectedCategoryID", cID);
            String name = category.getCategoryName().toLowerCase();

            boolean isBoarding = name.contains("boarding");
            boolean isCheckup  = name.contains("checkup");
            boolean needsVet   = !isBoarding && !isCheckup;

            request.setAttribute("isBoarding", isBoarding);
            request.setAttribute("isCheckup", isCheckup);
            request.setAttribute("needsVet", needsVet);

            request.setAttribute("serviceList",
                    serviceDAO.getServicesByCategoryID(cID));

            if (needsVet) {
                request.setAttribute("listPerson", userDAO.getAllVeterinarians());
                String vetUserIDStr = request.getParameter("assigneeInfo");
                String startDateStr = request.getParameter("startDate");

                if (vetUserIDStr != null && !vetUserIDStr.isEmpty() && startDateStr != null && !startDateStr.isEmpty()) {
                    try {
                        int vetUserID = Integer.parseInt(vetUserIDStr);
                        Integer vetID = userDAO.getVetIDByUserID(vetUserID);

                        if (vetID != null) {
                            java.sql.Date fromDate = java.sql.Date.valueOf(startDateStr);

                            List<TimeSlot> slotList = slotDAO.getSlotsNext7Days1(vetID, fromDate);

                            request.setAttribute("slotList", slotList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}