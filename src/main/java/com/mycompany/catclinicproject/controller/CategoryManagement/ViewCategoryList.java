/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.CategoryManagement;


import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.Category;
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
@WebServlet(name="ViewCategoryList", urlPatterns={"/ViewCategoryList"})
public class ViewCategoryList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDao dao = new CategoryDao();
        List<Category> fullList = dao.getCategory();

        String keyword = request.getParameter("search");
        String filterStatus = request.getParameter("status");

        // ===== SEARCH + FILTER =====
        List<Category> filteredList = new java.util.ArrayList<>();

        for (Category c : fullList) {

            boolean matchKeyword = true;
            boolean matchStatus = true;

            // SEARCH categoryName
            if (keyword != null && !keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase().trim();
                String name = c.getCategoryName() != null ? c.getCategoryName().toLowerCase() : "";

                if (!name.contains(k)) {
                    matchKeyword = false;
                }
            }

            // FILTER STATUS
            if (filterStatus != null && !filterStatus.equals("ALL") && !filterStatus.isEmpty()) {

                if (filterStatus.equals("Active") && !c.isActive()) {
                    matchStatus = false;
                }

                if (filterStatus.equals("Inactive") && c.isActive()) {
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

        List<Category> pagedList = new java.util.ArrayList<>();

        if (totalRecord > 0 && start < totalRecord) {
            pagedList = filteredList.subList(start, end);
        }

        // ===== SEND TO JSP =====
        request.setAttribute("categoryList", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("currentStatus", filterStatus);

        request.getRequestDispatcher("WEB-INF/views/manager/categoryList.jsp").forward(request, response);
    }
}

