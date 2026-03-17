package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BookingHistoryController", urlPatterns = {"/booking-history"})
public class BookingHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 5) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        BookingDAO dao = new BookingDAO();
        int userID = user.getUserID();
        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        int pageSize = 5;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        int offset = (currentPage - 1) * pageSize;

        List<BookingHistoryDTO> pagedList = dao.getClientHistoryPaging(userID, keyword, filterStatus, offset, pageSize);

        int totalRecord = dao.countClientBookings(userID, keyword, filterStatus);
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        List<BookingHistoryDTO> fullList = dao.getHistoryByUserID(userID);
        int total = fullList.size();
        int confirmedCount = 0;
        int completed = 0;
        int pendingPaymentCount = 0;
        int cancelledCount = 0;
        int pendingCancelCount = 0;

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        for (BookingHistoryDTO b : fullList) {
            // Logic cập nhật trạng thái ảo Completed nếu quá ngày
            LocalDate checkDate = (b.getEndDate() != null) ? b.getEndDate().toLocalDate() :
                    ((b.getAppointmentDate() != null) ? b.getAppointmentDate().toLocalDate() : null);

            if (checkDate != null && checkDate.isBefore(today) && "Confirmed".equalsIgnoreCase(b.getStatus())) {
                b.setStatus("Completed");
            }

            String s = b.getStatus();
            if (s != null) {
                if (s.equalsIgnoreCase("PendingPayment")) pendingPaymentCount++;
                else if (s.equalsIgnoreCase("Confirmed")) confirmedCount++;
                else if (s.equalsIgnoreCase("Completed")) completed++;
                else if (s.equalsIgnoreCase("Cancelled")) cancelledCount++;
                else if (s.equalsIgnoreCase("PendingCancel")) pendingCancelCount++;
            }
        }

        request.setAttribute("user", user);
        request.setAttribute("bookingList", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);

        request.setAttribute("pendingPaymentCount", pendingPaymentCount);
        request.setAttribute("cancelledCount", cancelledCount);
        request.setAttribute("total", total);
        request.setAttribute("confirmedCount", confirmedCount);
        request.setAttribute("completed", completed);
        request.setAttribute("pendingCancelCount", pendingCancelCount);

        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("/WEB-INF/views/client/booking-history.jsp").forward(request, response);
    }
}