package com.mycompany.catclinicproject.controller.booking;

import com.mycompany.catclinicproject.dao.*;
import com.mycompany.catclinicproject.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalTime;

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
        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.autoCancelExpiredBookings();

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
        String message = "";

        try {

            int catID = Integer.parseInt(request.getParameter("catID"));
            int serviceID = Integer.parseInt(request.getParameter("serviceID"));
            int assigneeID = Integer.parseInt(request.getParameter("assigneeInfo"));
            String slotParam = request.getParameter("slotID");
            String startDateParam = request.getParameter("startDate");
            String endDateParam = request.getParameter("endDate");
            String checkInTimeParam = request.getParameter("checkInTime");
            String note = request.getParameter("note");

            BookingDAO bookingDAO = new BookingDAO();
            ServiceDAO serviceDAO = new ServiceDAO();
            UserDAO userDAO = new UserDAO();

            Booking booking = new Booking();
            booking.setCatID(catID);
            booking.setNote(note);

            List<Integer> serviceIDs = new ArrayList<>();
            List<Double> prices = new ArrayList<>();
            double totalAmount = 0;

            User assignee = userDAO.getUserByID(assigneeID);


            //  (VETERINARIAN) dich vu kham
            if (assignee.getRoleID() == 2) {

                if (slotParam == null || slotParam.isEmpty()) {
                    message = "please choose slot!";
                }

                int slotID = Integer.parseInt(slotParam);

                // check CAt co lịch nao trufng giờ hay chua
                if (bookingDAO.isCatMedicalConflict(catID, slotID)) {
                    message ="Cat has an appointment at this time!";
                }

                Integer vetID = userDAO.getVetIDByUserID(assigneeID);
                if (vetID == null){
                    message ="Not found VET!";
                }

                TimeSlotDAO slotDAO = new TimeSlotDAO();
                TimeSlot slot = slotDAO.getSlotByID(slotID);

                booking.setVeterinarianID(vetID);
                booking.setStaffID(0);
                booking.setSlotID(slotID);
                booking.setAppointmentDate(slot.getSlotDate());
                booking.setEndDate(slot.getSlotDate());
                booking.setAppointmentTime(slot.getStartTime());

                Service svc = serviceDAO.getServiceById(serviceID);
                totalAmount = svc.getPrice();

                serviceIDs.add(serviceID);
                prices.add(totalAmount);
            }

            //  PET HOTEL

            else {

                if (startDateParam == null || endDateParam == null || checkInTimeParam == null) {
                    message ="Please select full date and time!";
                }

                LocalDate start = LocalDate.parse(startDateParam);
                LocalDate end = LocalDate.parse(endDateParam);
                LocalTime checkInTime = LocalTime.parse(checkInTimeParam);

                if (end.isBefore(start)) {
                    message ="Error Enddate";
                }

                //  CHECK CAT HOTEL CONFLICT
                if (bookingDAO.isCatHotelConflict(catID, java.sql.Date.valueOf(start), java.sql.Date.valueOf(end), java.sql.Time.valueOf(checkInTime))) {

                    message ="The cat already has a hotel booking for this period!";
                }

                Integer staffID = userDAO.getStaffIDByUserID(assigneeID);
                if (staffID == null){
                    message ="Not found staff!";
                }


                long numDays = java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;

                Service svc = serviceDAO.getServiceById(serviceID);
                totalAmount = svc.getPrice() * numDays;

                booking.setVeterinarianID(0);
                booking.setStaffID(staffID);
                booking.setSlotID(0); // pethotel thif k dufng slot
                booking.setAppointmentDate(java.sql.Date.valueOf(start));
                booking.setEndDate(java.sql.Date.valueOf(end));
                booking.setAppointmentTime(java.sql.Time.valueOf(checkInTime));

                serviceIDs.add(serviceID);
                prices.add(totalAmount);
            }

            // tao booking vaf  Bookingdetail vaf invoice
            int bookingID = bookingDAO.createBookingWithInvoice(booking, serviceIDs, prices, totalAmount);

            if (bookingID == -1) {
                message ="Unable to create a booking. The slot may already be taken.";
            }

            response.sendRedirect(request.getContextPath() + "/vnpay?bookingID=" + bookingID);

        } catch (Exception e) {

            request.setAttribute("error", message);
            loadBookingData(request, user);
            request.getRequestDispatcher("/WEB-INF/views/client/Booking.jsp")
                    .forward(request, response);
        }
    }

    private void loadBookingData(HttpServletRequest request, User user) {
        CatDAO catDAO = new CatDAO();
        TimeSlotDAO slotDAO = new TimeSlotDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        UserDAO userDAO = new UserDAO();

        // data mèo và phân trang
        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());
        int indexPage = request.getParameter("indexPage") != null ? Integer.parseInt(request.getParameter("indexPage")) : 1;
        request.setAttribute("catList", catDAO.getCatsByOwnerPaging(ownerID, indexPage, 5));
        request.setAttribute("totalPage", (int) Math.ceil((double) catDAO.countCats(ownerID) / 5));
        request.setAttribute("indexPage", indexPage);


        request.setAttribute("serviceList", serviceDAO.getAllServices());
        List<UserDTO> allPerson = userDAO.getVetAndCareStaff();
        List<UserDTO> listPerson = filterPersonByService(request, serviceDAO, allPerson);
        request.setAttribute("listPerson", listPerson);
        boolean isPetHotel = isPetHotelService(request, serviceDAO);
        request.setAttribute("isPetHotelService", isPetHotel);

        // Truyền service đã chọn để hiển thị giá/ngày cho Pet Hotel
        String serviceParam = request.getParameter("serviceID");
        if (serviceParam != null && !serviceParam.isEmpty()) {
            try {
                Service sel = serviceDAO.getServiceById(Integer.parseInt(serviceParam));
                if (sel != null)
                {
                    request.setAttribute("selectedService", sel);
                }
            } catch (Exception ignored) {

            }
        }

        //  Load lịch slot
        String assigneeParam = request.getParameter("assigneeInfo");
        String startDateParam = request.getParameter("startDate");
        if (startDateParam == null || startDateParam.isEmpty()) {
            startDateParam = LocalDate.now().toString();
        }

        Map<String, List<TimeSlot>> groupedSlots = new LinkedHashMap<>();
        String isStaff = "false";

        if (assigneeParam != null && !assigneeParam.isEmpty()) {
            int assigneeID = Integer.parseInt(assigneeParam);

            // Kiểm tra role người phụ trách (Care Staff, Veterinarian = Bác sĩ)
            for (UserDTO p : listPerson) {
                if (p.getUserID() == assigneeID) {
                    if ("Care".equalsIgnoreCase(p.getType()) ) {
                        isStaff = "true";
                    }
                    break;
                }
            }

            // Chỉ load slot nếu là Bác sĩ (chuyển UserID -> VetID)
            if ("false".equals(isStaff)) {
                Integer vetID = userDAO.getVetIDByUserID(assigneeID);
                if (vetID != null) {
                    List<TimeSlot> flatList = slotDAO.getSlotsNext7DaysFromDate(vetID, startDateParam);
                    for (TimeSlot s : flatList) {
                        String dateKey = s.getSlotDate().toString(); // lấy ngày làm key
                        List<TimeSlot> listByDate = groupedSlots.get(dateKey);
                        // check nếu trong map chưa có value của key
                        if (listByDate == null) {
                            listByDate = new ArrayList<>();
                            groupedSlots.put(dateKey, listByDate);
                        }
                        listByDate.add(s);
                    }
                }
            }
        }

        request.setAttribute("slotListGrouped", groupedSlots);
        request.setAttribute("isStaff", isStaff);
        request.setAttribute("currentDate", LocalDate.now().toString());
    }

   // check sẻvice is pet hotel
    private boolean isPetHotelService(HttpServletRequest request, ServiceDAO serviceDAO) {
        String serviceParam = request.getParameter("serviceID");
        if (serviceParam == null || serviceParam.isEmpty()) {
            return false;
        }
        try {
            int serviceID = Integer.parseInt(serviceParam);
            Service svc = serviceDAO.getServiceById(serviceID);
            if (svc != null && svc.getNameService() != null) {
                String name = svc.getNameService().toLowerCase();
                return name.contains("pet hotel") ;
            }
        } catch (Exception ignored) {

        }
        return false;
    }

    // filter người phụ trách bơir dịch vụ
    private List<UserDTO> filterPersonByService(HttpServletRequest request, ServiceDAO serviceDAO, List<UserDTO> allPerson) {

        boolean isPetHotel = isPetHotelService(request, serviceDAO);
        List<UserDTO> filtered = new ArrayList<>();
        for (UserDTO p : allPerson) {
            if (isPetHotel) {
                if ("Care".equalsIgnoreCase(p.getType()) ) {
                    filtered.add(p);
                }
            } else {
                if ("Veterinarian".equalsIgnoreCase(p.getType())) {
                    filtered.add(p);
                }
            }
        }
        return filtered;
    }
}