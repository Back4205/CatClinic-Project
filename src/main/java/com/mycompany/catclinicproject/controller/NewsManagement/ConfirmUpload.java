/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.NewsManagement;

import com.mycompany.catclinicproject.dao.NewDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
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
@WebServlet(name="ConfirmUpload", urlPatterns={"/ConfirmUpload"})
public class ConfirmUpload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        new NewDAO().updateStatus(id, true); // Đổi thành Active
        response.sendRedirect("ViewNewList");
    }
}
