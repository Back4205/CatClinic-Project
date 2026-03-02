package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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

        // --- HARDCODED USER ID = 5 ---
      //  int userID = 5;
        HttpSession session = request.getSession(false);
     User user = (User)session.getAttribute("acc");
     if(user == null){
         response.sendRedirect(request.getContextPath()+"/login");
         return;
     }
     int userID = user.getUserID();


        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        BookingDAO dao = new BookingDAO();
        List<BookingHistoryDTO> fullList = dao.getHistoryByUserID(userID);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        for (BookingHistoryDTO b : fullList) {
            LocalDate checkDate;
            if (b.getEndDate() != null) {
                checkDate = b.getEndDate().toLocalDate();
            } else {
                if (b.getAppointmentDate() != null) {
                    checkDate = b.getAppointmentDate().toLocalDate();
                } else {
                    continue;
                }
            }

            if (checkDate.isBefore(today) && "Confirmed".equalsIgnoreCase(b.getStatus())) {
                b.setStatus("Completed");
            }
        }

        int total = fullList.size();
        int scheduled = 0;
        int completed = 0;

        for (BookingHistoryDTO b : fullList) {
            String s = b.getStatus();

            if (s != null) {
                if (s.equalsIgnoreCase("Confirmed") || s.equalsIgnoreCase("Pending") || s.equalsIgnoreCase("Upcoming")) {
                    scheduled++;
                } else if (s.equalsIgnoreCase("Completed") || s.equalsIgnoreCase("Done")) {
                    completed++;
                }
            }
        }

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

        request.setAttribute("user", user);
        request.setAttribute("bookingList", filteredList); // Filtered list
        request.setAttribute("total", total);
        request.setAttribute("scheduled", scheduled);
        request.setAttribute("completed", completed);

        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("/WEB-INF/views/client/booking-history.jsp").forward(request, response);
    }
}
