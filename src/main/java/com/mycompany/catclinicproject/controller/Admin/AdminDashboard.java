package com.mycompany.catclinicproject.controller.Admin;

import com.mycompany.catclinicproject.dao.DashboardDAO;
import com.mycompany.catclinicproject.model.DashboardDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminDashboard", urlPatterns = {"/AdminDashboard"})
public class AdminDashboard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        DashboardDAO dao = new DashboardDAO();
        DashboardDTO data = dao.getDashboardStats();
        request.setAttribute("DASHBOARD_DATA", data);
        request.getRequestDispatcher("WEB-INF/views/manager/AdminDashboard.jsp").forward(request, response);
    }

}