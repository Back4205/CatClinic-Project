/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.dao.ServiceDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HideService", urlPatterns = {"/HideService"})
public class HideService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int ServiceID = Integer.parseInt(request.getParameter("serviceID"));
            int categoryID =  Integer.parseInt(request.getParameter("categoryID"));
            String action = request.getParameter("action");
            ServiceDAO dao = new ServiceDAO();
            if ("hide".equals(action)) {
                dao.updateServiceStatus(ServiceID, false); // hide → IsActive = false
            } else if ("show".equals(action)) {
                dao.updateServiceStatus(ServiceID, true);  // show → IsActive = true
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.sendRedirect("ViewServiceList?id=" + categoryID);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}