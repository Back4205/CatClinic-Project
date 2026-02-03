/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.Service;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ViewServiceList", urlPatterns = {"/ViewServiceList"})
public class ViewServiceList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServiceDAO sdao = new ServiceDAO();
        List<Service> serviceList = sdao.getAllServices();
        request.setAttribute("serviceList", serviceList);
        request.getRequestDispatcher("WEB-INF/views/manager/serviceList.jsp").forward(request, response);
    }
}
