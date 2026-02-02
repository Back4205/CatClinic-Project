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
        int userID = 5;
//        HttpSession session = request.getSession(false);
//     User user = (User)session.getAttribute("acc");
//     if(user == null){
//         response.sendRedirect(request.getContextPath()+"/login");
//         return;
//     }
//     int userID = user.getUserID();

        String keyword = request.getParameter("search"); 
        String filterStatus = request.getParameter("status"); 

        BookingDAO dao = new BookingDAO();
        List<BookingHistoryDTO> fullList = dao.getHistoryByUserID(userID);
        
        // Get current date in Vietnam time
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")); 
        
        // Loop: Iterate through each booking to check and update its status based on the date
        for (BookingHistoryDTO b : fullList) {
            // Define variable to hold the date we need to compare
            LocalDate checkDate;
            
            // Condition: Check if the booking has an end date (e.g., Pet Hotel service)
            if (b.getEndDate() != null) {
                checkDate = b.getEndDate().toLocalDate();
            } 
            // Condition: Executed if there is no end date (e.g., Clinic service uses AppointmentDate)
            else {
                // Condition: Check if the appointment date exists
                if (b.getAppointmentDate() != null) {
                    checkDate = b.getAppointmentDate().toLocalDate();
                } 
                // Condition: Skip this iteration if the date data is missing or invalid
                else {
                    continue; 
                }
            }
            
            // Logic: Only mark as 'Completed' if the check date is in the past
            // Condition: Check if the date is before today AND the current status is 'Confirmed'
            if (checkDate.isBefore(today) && "Confirmed".equalsIgnoreCase(b.getStatus())) {
                b.setStatus("Completed");
            }
        }
        
        int total = fullList.size();
        int scheduled = 0;
        int completed = 0;
        
        // Loop: Iterate through the list again to count bookings for the dashboard stats
        for (BookingHistoryDTO b : fullList) {
            String s = b.getStatus();
            // Condition: Ensure the status is not null to avoid errors
            if (s != null) {
                // Condition: Check if the status is active (Confirmed, Pending, or Upcoming)
                if (s.equalsIgnoreCase("Confirmed") || s.equalsIgnoreCase("Pending") || s.equalsIgnoreCase("Upcoming")) {
                    scheduled++;
                } 
                // Condition: Check if the status is finished (Completed or Done)
                else if (s.equalsIgnoreCase("Completed") || s.equalsIgnoreCase("Done")) {
                    completed++;
                }
            }
        }

        List<BookingHistoryDTO> filteredList = new ArrayList<>();
        
        // Loop: Iterate through the full list to filter results
        for (BookingHistoryDTO b : fullList) {
            boolean isMatchKeyword = true;
            boolean isMatchStatus = true;

            // -- Filter by Keyword (Cat Name or Service Name) --
            // Condition: Check if the user entered a search keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String catName = (b.getCatName() != null) ? b.getCatName().toLowerCase() : "";
                String service = (b.getServiceName() != null) ? b.getServiceName().toLowerCase() : "";
                
                // Condition: Check if the keyword does NOT match either the cat name or the service name
                if (!catName.contains(k) && !service.contains(k)) {
                    isMatchKeyword = false;
                }
            }

            // -- Filter by Status (Button Click) --
            // Condition: Check if the user selected a specific status filter (not ALL)
            if (filterStatus != null && !filterStatus.equals("ALL") && !filterStatus.isEmpty()) {
                // Condition: Check if the booking status does NOT match the selected filter
                if (b.getStatus() == null || !b.getStatus().equalsIgnoreCase(filterStatus)) {
                    isMatchStatus = false;
                }
            }

            // Condition: Add to the result list only if both search and status conditions are met
            if (isMatchKeyword && isMatchStatus) {
                filteredList.add(b);
            }
        }

        request.setAttribute("bookingList", filteredList); // Filtered list
        request.setAttribute("total", total);
        request.setAttribute("scheduled", scheduled);
        request.setAttribute("completed", completed);
        
        // Send back parameters to keep input values in the form
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("/WEB-INF/views/client/booking-history.jsp").forward(request, response);
    }
}