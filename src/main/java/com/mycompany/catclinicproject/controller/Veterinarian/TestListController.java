/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.homeDao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.model.TestOrderViewDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "TestListController", urlPatterns = {"/testlist"})
public class TestListController extends HttpServlet {

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
            out.println("<title>Servlet TestListController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestListController at " + request.getContextPath() + "</h1>");
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

    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    BookingDaoVeterinarian dao = new BookingDaoVeterinarian();

    int vetID = dao.getVetIDByUserID(user.getUserID());

    int page = 1;
    int pageSize = 5;

    String pageParam = request.getParameter("page");

    if (pageParam != null) {
        page = Integer.parseInt(pageParam);
    }


    List<TestOrderViewDTO> list =
            dao.getTestOrdersByVetID(vetID, page, pageSize);

    int totalRecord = dao.countTestOrdersByVetID(vetID);

    int totalPage = totalRecord / pageSize;
    if (totalRecord % pageSize != 0) {
        totalPage++;
    }

    request.setAttribute("testList", list);
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPage", totalPage);
        request.setAttribute("activePage", "testlist");


    request.getRequestDispatcher("WEB-INF/views/veterinarian/testrequest.jsp")
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
