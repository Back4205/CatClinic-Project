package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.util.ArrayList;
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

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("acc");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        BookingDAO dao = new BookingDAO();

        // XỬ LÝ XEM CHI TIẾT
        if ("detail".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                BookingHistoryDTO booking = dao.getBookingDetailByID(id);
                if (booking != null) {
                    request.setAttribute("booking", booking);
                    request.getRequestDispatcher("/WEB-INF/views/client/booking-detail.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // LẤY DANH SÁCH LỊCH SỬ
        int userID = user.getUserID();
        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        // Hàm này trong DAO đã tự động chạy cancelNoShowBookings và autoCancelExpiredBookings
        List<BookingHistoryDTO> fullList = dao.getHistoryByUserID(userID);

        // THỐNG KÊ TRẠNG THÁI (STATS)
        int total = fullList.size();
        int scheduled = 0;
        int completed = 0;
        int pendingPaymentCount = 0;
        int cancelledCount = 0;
        int pendingRefundCount = 0; // Thêm biến đếm cho trạng thái hoàn tiền mới

        for (BookingHistoryDTO b : fullList) {
            String s = b.getStatus();
            if (s != null) {
                if (s.equalsIgnoreCase("PendingPayment")) {
                    pendingPaymentCount++;
                } else if (s.equalsIgnoreCase("Confirmed") || s.equalsIgnoreCase("Upcoming")) {
                    scheduled++;
                } else if (s.equalsIgnoreCase("Completed")) {
                    completed++;
                } else if (s.equalsIgnoreCase("Cancelled")) {
                    cancelledCount++;
                } else if (s.equalsIgnoreCase("Pending Refund")) {
                    pendingRefundCount++;
                }
            }
        }

        // LỌC DỮ LIỆU (FILTER)
        List<BookingHistoryDTO> filteredList = new ArrayList<>();
        for (BookingHistoryDTO b : fullList) {
            boolean isMatchKeyword = true;
            boolean isMatchStatus = true;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String catName = (b.getCatName() != null) ? b.getCatName().toLowerCase() : "";
                String service = (b.getServiceName() != null) ? b.getServiceName().toLowerCase() : "";
                if (!catName.contains(k) && !service.contains(k)) {
                    isMatchKeyword = false;
                }
            }

            if (filterStatus != null && !filterStatus.equals("ALL") && !filterStatus.isEmpty()) {
                if (b.getStatus() == null || !b.getStatus().equalsIgnoreCase(filterStatus)) {
                    isMatchStatus = false;
                }
            }

            if (isMatchKeyword && isMatchStatus) {
                filteredList.add(b);
            }
        }

        // PHÂN TRANG (PAGINATION)
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

        int totalRecord = filteredList.size();
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);
        if (currentPage > totalPage && totalPage != 0) currentPage = totalPage;

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecord);
        List<BookingHistoryDTO> pagedList = (totalRecord > 0) ? filteredList.subList(start, end) : new ArrayList<>();

        // GỬI DỮ LIỆU SANG JSP
        request.setAttribute("user", user);
        request.setAttribute("bookingList", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("total", total);
        request.setAttribute("scheduled", scheduled);
        request.setAttribute("completed", completed);
        request.setAttribute("pendingPaymentCount", pendingPaymentCount);
        request.setAttribute("cancelledCount", cancelledCount);
        request.setAttribute("pendingRefundCount", pendingRefundCount);
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("/WEB-INF/views/client/booking-history.jsp").forward(request, response);
    }
}