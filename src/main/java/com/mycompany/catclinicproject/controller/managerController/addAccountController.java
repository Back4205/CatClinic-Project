/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.managerController;

import com.mycompany.catclinicproject.dao.homeDao.UserDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Son
 */
public class addAccountController extends HttpServlet {

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
            out.println("<title>Servlet addAccountController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addAccountController at " + request.getContextPath() + "</h1>");
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

    if (session.getAttribute("success") != null) {
        request.setAttribute("success", session.getAttribute("success"));
        session.removeAttribute("success");
    }

    if (session.getAttribute("error") != null) {
        request.setAttribute("error", session.getAttribute("error"));
        session.removeAttribute("error");
    }

    request.getRequestDispatcher("/WEB-INF/views/manager/addAccount.jsp")
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

    HttpSession session = request.getSession();

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");
    String role = request.getParameter("role");
    String gender = request.getParameter("gender");

    boolean male = "Male".equalsIgnoreCase(gender);

    UserDao dao = new UserDao();

    if (dao.isUsernameExist(username)) {
        session.setAttribute("error", "Username already exists!");
        response.sendRedirect("addAccount");
        return;
    }

    int roleID = dao.getRoleID(role);

    boolean ok = dao.addUser(username, password, fullName, male,
                             email, roleID, phone, null);

    if (ok) {
        session.setAttribute("success", "Add user successfully!");
    } else {
        session.setAttribute("error", "Add user failed!");
    }

    response.sendRedirect("addAccount");
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
