package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BillingBookingDTO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BookingListController", urlPatterns = {"/view-booking-list"})
public class BookingListController extends HttpServlet {

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
        BookingDAO dao = new BookingDAO();
        int pageSize = 5;

        String status = request.getParameter("status");
        String search = request.getParameter("search");
        String pageParam = request.getParameter("page");

        String dateFilter = request.getParameter("dateFilter");
        if (dateFilter == null || dateFilter.trim().isEmpty()) {
            dateFilter = java.time.LocalDate.now().toString();
//            dateFilter = null ;
        }


        if (status == null) {
            status = "";
        }
        if (search == null) {
            search = "";
        }

        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        int offset = (currentPage - 1) * pageSize;

        List<BookingHistoryDTO> list = dao.getBookingHistory(search, status, dateFilter, offset, pageSize);

        int totalRecord = dao.countBookings(search, status, dateFilter);
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("Total", dao.countTotalBookings(dateFilter));
        stats.put("PendingPayment", dao.countPendingPayment(dateFilter));
        stats.put("Confirmed", dao.countConfirmed(dateFilter));
        stats.put("Completed", dao.countCompleted(dateFilter));
        stats.put("PendingCancelRefund", dao.countPendingCancelRefund(dateFilter));
        stats.put("Cancelled", dao.countCancelled(dateFilter));
        // ----------------------------------------------------------------------

        request.setAttribute("bookingList", list);
        request.setAttribute("stats", stats);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentSearch", search);
        request.setAttribute("currentStatus", status);

        request.setAttribute("currentDate", dateFilter);

        request.getRequestDispatcher("WEB-INF/views/reception/view-booking-list.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}