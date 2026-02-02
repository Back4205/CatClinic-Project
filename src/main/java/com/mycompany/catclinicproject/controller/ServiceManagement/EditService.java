/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EditService", urlPatterns = {"/EditService"})
public class EditService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        ServiceDAO dao = new ServiceDAO();
        Service service = dao.getServiceById(id);

        request.setAttribute("service", service);
        request.getRequestDispatcher("/WEB-INF/views/manager/serviceEdit.jsp")
                .forward(request, response);
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    int id = Integer.parseInt(request.getParameter("id"));
    String name = request.getParameter("name");
    double price = Double.parseDouble(request.getParameter("price"));
    String description = request.getParameter("description");
    int time = Integer.parseInt(request.getParameter("time"));

    Service s = new Service();
    s.setServiceID(id);
    s.setNameService(name);
    s.setPrice(price);
    s.setDescription(description);
    s.setTimeService(time);

    ServiceDAO dao = new ServiceDAO();
    dao.updateService(s);

    response.sendRedirect(request.getContextPath() + "/ViewServiceList");
}
}