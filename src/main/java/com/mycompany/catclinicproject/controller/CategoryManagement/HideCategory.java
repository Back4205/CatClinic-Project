/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.CategoryManagement;

import com.mycompany.catclinicproject.dao.CategoryDAO;
import com.mycompany.catclinicproject.dao.ServiceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="HideCategory", urlPatterns={"/HideCategory"})
public class HideCategory extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String action = request.getParameter("action");

            CategoryDAO dao = new CategoryDAO();

            if ("hide".equals(action)) {
                dao.updateCategoryStatus(id, false); // hide → IsActive = false
            } else if ("show".equals(action)) {
                dao.updateCategoryStatus(id, true);  // show → IsActive = true
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/ViewCategoryList");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}