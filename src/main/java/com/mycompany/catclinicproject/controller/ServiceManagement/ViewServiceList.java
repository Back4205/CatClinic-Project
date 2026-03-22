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

        int categoryID = Integer.parseInt(request.getParameter("id"));

        ServiceDAO dao = new ServiceDAO();
        List<Service> fullList = dao.getServicesByCategoryID(categoryID);

        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        if (keyword == null) keyword = "";
        if (filterStatus == null) filterStatus = "ALL";

        // ===== SEARCH + FILTER =====
        List<Service> filteredList = new java.util.ArrayList<>();

        for (Service s : fullList) {

            boolean matchKeyword = true;
            boolean matchStatus = true;

            // SEARCH nameService
            if (!keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase();
                String name = s.getNameService() != null ? s.getNameService().toLowerCase() : "";

                if (!name.contains(k)) {
                    matchKeyword = false;
                }
            }

            // FILTER STATUS
            if (!filterStatus.equals("ALL")) {

                if (filterStatus.equals("Active") && !s.isIsActive()) {
                    matchStatus = false;
                }

                if (filterStatus.equals("Inactive") && s.isIsActive()) {
                    matchStatus = false;
                }
            }

            if (matchKeyword && matchStatus) {
                filteredList.add(s);
            }
        }

        // ===== PAGINATION =====
        int pageSize = 5;
        int currentPage = 1;

        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (Exception e) {
                currentPage = 1;
            }
        }

        int totalRecord = filteredList.size();
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        if (totalPage == 0) totalPage = 1;

        if (currentPage > totalPage) currentPage = totalPage;

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecord);

        List<Service> pagedList = new java.util.ArrayList<>();

        if (totalRecord > 0) {
            pagedList = filteredList.subList(start, end);
        }

        // ===== SEND DATA =====
        request.setAttribute("serviceList", pagedList);
        request.setAttribute("categoryID", categoryID);

        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);

        request.getRequestDispatcher("WEB-INF/views/manager/serviceList.jsp").forward(request, response);
    }
}
