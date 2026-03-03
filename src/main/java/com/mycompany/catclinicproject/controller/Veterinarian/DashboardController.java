/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.homeDao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.model.AssignCaseDTO;
import com.mycompany.catclinicproject.model.DetailBookingDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Son
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/DashboardController"})
public class DashboardController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DashboardController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashboardController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        // Nếu chưa login
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();

        // Lấy VetID từ UserID
        int vetID = dao.getVetIDByUserID(user.getUserID());

        if (vetID == -1) {
            response.sendRedirect("login");
            return;
        }
        String keyword = request.getParameter("keyword");
        request.setAttribute("keyword", keyword);
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");

        LocalDate today = LocalDate.now();
        String todayStr = today.toString();

        if (dateFrom == null || dateFrom.isEmpty()) {
            dateFrom = todayStr;
        }

        if (dateTo == null || dateTo.isEmpty()) {
            dateTo = todayStr;
        }
        if (dateFrom != null && dateTo != null) {
            if (dateFrom.compareTo(dateTo) > 0) {
                String temp = dateFrom;
                dateFrom = dateTo;
                dateTo = temp;
            }
        }

        int page = 1;
        int pageSize = 5;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (Exception e) {
                page = 1;
            }
        }
        // ===== GỌI DAO =====
        int totalRecords = dao.countAssignCases(vetID, dateFrom, dateTo);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        List<AssignCaseDTO> assignList
                = dao.getAssignCasesPaging(vetID, dateFrom, dateTo, keyword, page, pageSize);
        int confirmedCount = dao.countByStatusWithDate(
                vetID, "Completed", dateFrom, dateTo);
        int inTreatmentCount = dao.countByStatusWithDate(
                vetID, "In Treatment", dateFrom, dateTo);
        int completedCount = dao.countByStatusWithDate(
                vetID, "Completed", dateFrom, dateTo);
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);
        String bookingIDRaw = request.getParameter("bookingID");
        String raw = request.getParameter("bookingID");

        if (raw != null && !raw.trim().isEmpty()) {
            int bookingID = Integer.parseInt(raw.trim());
        }
        if (bookingIDRaw != null) {
            int bookingID = Integer.parseInt(bookingIDRaw);

            DetailBookingDTO detail = dao.getBookingDetail(bookingID);
            request.setAttribute("selectedCase", detail);
        }
        String formattedDate = today.format(formatter);
        request.setAttribute("todayDate", formattedDate);
        request.setAttribute("confirmedCount", confirmedCount);
        request.setAttribute("inTreatmentCount", inTreatmentCount);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("assignList", assignList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("dateFrom", dateFrom);
        request.setAttribute("dateTo", dateTo);
        request.setAttribute("activePage", "dashboard");
        request.getRequestDispatcher("WEB-INF/views/veterinarian/dashboard.jsp")
                .forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
