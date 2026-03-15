package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
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
        BookingDAO dao = new BookingDAO();
        int pageSize = 5;
        String status = request.getParameter("status");
        String search = request.getParameter("search");
        String pageParam = request.getParameter("page");
        if (status == null) {
            status = "";
        }
        if (search == null) {
            search = "";
        }
        int currentPage = 1;
        if (pageParam != null) {
            currentPage = Integer.parseInt(pageParam);
        }
        int offset = (currentPage - 1) * pageSize;
        List<BookingHistoryDTO> list = dao.getBookingHistory(search, status, offset, pageSize);
        int totalRecord = dao.countBookings(search, status);
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);
        Map<String, Integer> stats = new HashMap<>();
        stats.put("PendingPayment", dao.countPendingPayment());
        stats.put("Confirmed", dao.countConfirmed());
        stats.put("Completed", dao.countCompleted());
        stats.put("PendingCancelRefund", dao.countPendingCancelRefund());
        stats.put("Cancelled", dao.countCancelled());
        request.setAttribute("bookingList", list);
        request.setAttribute("stats", stats);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentSearch", search);
        request.setAttribute("currentStatus", status);
        request.getRequestDispatcher("WEB-INF/views/reception/view-booking-list.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}





