package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.dao.PaymentDAO;
import com.mycompany.catclinicproject.model.BillingBookingDTO;
import com.mycompany.catclinicproject.model.Booking;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BillingBookingController", urlPatterns = {"/reception/billing-bookings"})
public class BillingBookingController extends HttpServlet {

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        BookingDAO bookingDAO = new BookingDAO();

        Integer lastSeenID = (Integer) session.getAttribute("lastSeenBookingID");
        if (lastSeenID == null) lastSeenID = 0;

        List<BillingBookingDTO> newBookings = bookingDAO.getNewBookings(lastSeenID);

        session.setAttribute("notifications", newBookings);
        session.setAttribute("notificationCount", newBookings.size());

        PaymentDAO dao = new PaymentDAO();


        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String filterDate = request.getParameter("filterDate");

        if (search == null) search = "";
        if (status == null || status.isEmpty()) status = "ALL";



        int page = -1;

        try {
            page = Integer.parseInt(request.getParameter("page"));
            if (page < 1) page = 1;
        } catch (Exception e) {
            page = 1;
        }



        List<BillingBookingDTO> list = dao.getBillingBookings(search, status, filterDate, page, PAGE_SIZE);

        int totalRecord = dao.countBillingBookings(search, status, filterDate);

        int totalPage = (int) Math.ceil((double) totalRecord / PAGE_SIZE);

        request.setAttribute("billingList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentSearch", search);
        request.setAttribute("currentStatus", status);
        request.setAttribute("currentDate", filterDate);
        request.getRequestDispatcher("/WEB-INF/views/reception/billing_booking.jsp")
                .forward(request, response);
    }

}