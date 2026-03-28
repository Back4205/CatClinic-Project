/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.model.AssignCaseDTO2;
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
import java.util.List;
@WebServlet(name = "AssignCaseController", urlPatterns = {"/assignedCases"})
public class TreatmentRecordController extends HttpServlet {

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
            out.println("<title>Servlet AssignCaseController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AssignCaseController at " + request.getContextPath() + "</h1>");
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
         request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;
        if (user == null || user.getRoleID() != 2) {
            response.sendRedirect(request.getContextPath() + "/login?from=booking");
            return;
        }
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
        if (dateFrom.compareTo(dateTo) > 0) {
            String temp = dateFrom;
            dateFrom = dateTo;
            dateTo = temp;
        }
        int page = 1;
        int pageSize = 5;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        int vetID = dao.getVetIDByUserID(user.getUserID());
        int totalRecords = dao.countAssignedCasesByVetID(vetID, keyword, status);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        List<AssignCaseDTO2> list
                = dao.getAssignedCasesByVetID(vetID, keyword, status, page, pageSize);
        request.setAttribute("assignedCases", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("dateFrom", dateFrom);
        request.setAttribute("activePage", "assigned");
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);

        request.setAttribute("dateTo", dateTo);

        request.getRequestDispatcher("WEB-INF/views/veterinarian/treatmentrecords.jsp")
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
