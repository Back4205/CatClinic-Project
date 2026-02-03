/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.Service;
import java.io.IOException;

import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ViewServiceDetail", urlPatterns = {"/ViewServiceDetail"})
public class ViewServiceDetail extends HttpServlet {
   
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession(false);
         if (session == null || session.getAttribute("acc") == null) {
             response.sendRedirect(request.getContextPath() + "/login");
             return;
         }
         User user = (User) session.getAttribute("acc");
         if (user.getRoleID() != 1) {
             response.sendError(HttpServletResponse.SC_FORBIDDEN);
             return;
         }
        int id = Integer.parseInt(request.getParameter("id"));

        ServiceDAO dao = new ServiceDAO();
        Service service = dao.getServiceById(id);

        request.setAttribute("service", service);
        request.getRequestDispatcher("/WEB-INF/views/manager/serviceDetail.jsp").forward(request, response);
    }
}