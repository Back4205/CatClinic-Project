/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.NewsManagement;

import com.mycompany.catclinicproject.dao.NewDAO;
import com.mycompany.catclinicproject.model.News;
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
@WebServlet(name="ViewNewList", urlPatterns={"/ViewNewList"})
public class ViewNewList extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        NewDAO dao = new NewDAO();
        List<News> fullList = dao.getAllNew();

        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        // ===== SEARCH + FILTER =====
        List<News> filteredList = new java.util.ArrayList<>();

        for (News c : fullList) {

            boolean matchKeyword = true;
            boolean matchStatus = true;

            // SEARCH title
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String name = c.getTitle() != null ? c.getTitle().toLowerCase() : "";

                if (!name.contains(k)) {
                    matchKeyword = false;
                }
            }
            // FILTER STATUS
            if (filterStatus != null && !filterStatus.equals("ALL") && !filterStatus.isEmpty()) {

                if (filterStatus.equals("Active") && !c.isIsActive()) {
                    matchStatus = false;
                }

                if (filterStatus.equals("Inactive") && c.isIsActive()) {
                    matchStatus = false;
                }
            }

            if (matchKeyword && matchStatus) {
                filteredList.add(c);
            }
        }

        // ===== PAGINATION =====
        int pageSize = 5;

        int currentPage = 1;
        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int totalRecord = filteredList.size();
        int totalPage = (int) Math.ceil((double) totalRecord / pageSize);

        if (currentPage > totalPage && totalPage != 0) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecord);

        List<News> pagedList = new java.util.ArrayList<>();

        if (totalRecord > 0 && start < totalRecord) {
            pagedList = filteredList.subList(start, end);
        }

        // ===== SEND TO JSP =====
        request.setAttribute("newList", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("WEB-INF/views/manager/newList.jsp").forward(request, response);
    }

}
