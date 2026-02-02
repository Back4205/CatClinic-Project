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
import java.util.ArrayList;


@WebServlet(name = "SearchService", urlPatterns = {"/SearchService"})
public class SearchService extends HttpServlet {

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String nameService = request.getParameter("nameService");

    String statusRaw = request.getParameter("status");
    Boolean status = null;
    if (statusRaw != null && !statusRaw.equals("all")) {
        status = Boolean.parseBoolean(statusRaw);
    }

    String sortPrice = request.getParameter("sortPrice");

    int page = 1;
    int pageSize = 5;
    String pageRaw = request.getParameter("page");
    if (pageRaw != null) {
        page = Integer.parseInt(pageRaw);
    }

    ServiceDAO dao = new ServiceDAO();

    ArrayList<Service> list =
            dao.search(nameService, status, sortPrice, page, pageSize);

    int totalRecord = dao.count(nameService, status);
    int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

    request.setAttribute("listService", list);
    request.setAttribute("page", page);
    request.setAttribute("totalPage", totalPage);

    request.getRequestDispatcher("/WEB-INF/views/manager/serviceTable.jsp")
            .forward(request, response);
}

}
