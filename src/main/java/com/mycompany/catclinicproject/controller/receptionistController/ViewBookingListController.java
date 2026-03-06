package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ViewBookingListController", urlPatterns = {
        "/reception/view-booking-list",
        "/reception/home",
        "/reception/update-status",
        "/reception/appointment-detail"
})
public class ViewBookingListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("acc");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        BookingDAO dao = new BookingDAO();
        dao.cancelNoShowBookings();
        if (path.contains("appointment-detail")) {
            try {
                int bookingID = Integer.parseInt(request.getParameter("id"));
                BookingHistoryDTO detail = dao.getBookingDetailByID(bookingID);
                if (detail != null) {
                    request.setAttribute("booking", detail);
                    request.getRequestDispatcher("/WEB-INF/views/reception/appointment-detail.jsp").forward(request, response);
                } else {
                    response.sendRedirect("home");
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("home");
                return;
            }
        }

        else if (path.contains("update-status")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String status = request.getParameter("status");
                dao.updateBookingStatus(id, status);
                response.sendRedirect("home");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String searchQuery = request.getParameter("searchQuery");
        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        if (filterStatus == null || filterStatus.isEmpty()) {
            filterStatus = "ALL";
        }

        Map<String, Integer> stats = dao.getBookingDashboardStats();

        List<BookingHistoryDTO> fullList = dao.getReceptionBookingList(searchQuery);

        List<BookingHistoryDTO> filteredList = new ArrayList<>();

        for (BookingHistoryDTO b : fullList) {

            boolean isMatchKeyword = true;
            boolean isMatchStatus = true;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String catName = b.getCatName() != null ? b.getCatName().toLowerCase() : "";
                String service = b.getServiceName() != null ? b.getServiceName().toLowerCase() : "";

                if (!catName.contains(k) && !service.contains(k)) {
                    isMatchKeyword = false;
                }
            }

            if (!filterStatus.equals("ALL")) {
                if (b.getStatus() == null || !b.getStatus().equalsIgnoreCase(filterStatus)) {
                    isMatchStatus = false;
                }
            }

            if (isMatchKeyword && isMatchStatus) {
                filteredList.add(b);
            }
        }


        int pageSize = 5;
        int currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (Exception e) {
                currentPage = 1;
            }
        }

        int totalRecord = filteredList.size();
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        if (currentPage > totalPage && totalPage != 0) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecord);

        List<BookingHistoryDTO> pagedList = new ArrayList<>();
        if (totalRecord > 0 && start < totalRecord) {
            pagedList = filteredList.subList(start, end);
        }


        request.setAttribute("stats", stats);
        request.setAttribute("bookingList", pagedList);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("currentStatus", filterStatus);
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);

        request.getRequestDispatcher("/WEB-INF/views/reception/view-booking-list.jsp")
                .forward(request, response);
    }
}