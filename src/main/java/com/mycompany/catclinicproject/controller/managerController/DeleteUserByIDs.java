/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.managerController;

import com.mycompany.catclinicproject.dao.homeDao.UserDao;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.UserDTO;
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
@WebServlet(name = "DeleteUserByIDs", urlPatterns = {"/deleteUsers"})

public class DeleteUserByIDs extends HttpServlet {

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
            out.println("<title>Servlet DeleteUserByIDs</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteUserByIDs at " + request.getContextPath() + "</h1>");
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
          // ===== 1. CHECK LOGIN =====
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("acc") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("acc");

        // ===== 2. CHECK ROLE (ADMIN ONLY) =====
        if (currentUser.getRoleID() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            
        }

        // ===== 3. GET PARAM =====
        String idRaw = request.getParameter("id");
        String page = request.getParameter("page");
        String keyword = request.getParameter("keyword");
        String role = request.getParameter("role");

        if (page == null || page.isEmpty()) page = "1";
        if (keyword == null) keyword = "";
        if (role == null) role = "";

        try {
            int userID = Integer.parseInt(idRaw);

            UserDao dao = new UserDao();

            // Không cho xóa admin chính
            if (userID == 1) {
                session.setAttribute("error_account", "Cannot delete main Admin account!");
                  response.sendRedirect("account?page=" + page
                + "&keyword=" + keyword
                + "&role=" + role);
                return ;
            } 
            dao.DeleteUserById(userID);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error_account", "Delete failed!");
        }

        // ===== 4. REDIRECT BACK WITH STATE =====
        response.sendRedirect("account?page=" + page
                + "&keyword=" + keyword
                + "&role=" + role);
    

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
