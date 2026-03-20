/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.HomeController;

import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.dao.NewDAO;
import com.mycompany.catclinicproject.dao.homeDao.NewsDao;

import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.News;
import com.mycompany.catclinicproject.model.NewsDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "NewController", urlPatterns = {"/new"})

public class NewsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        NewDAO ndao = new NewDAO();        
        CategoryDao cdao = new CategoryDao();
        List<News> nlist = ndao.getActiveNew();
        List<Category> list = cdao.getAllCategory();
        List<News> top3List = ndao.getTop3LatestNews();
        String keyword = request.getParameter("search");
        if (keyword == null) keyword = "";
        // ===== SEARCH + FILTER =====
        List<News> filteredList = new java.util.ArrayList<>();

        for (News s : nlist) {

            boolean matchKeyword = true;
            // SEARCH title
            if (!keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase();
                String name = s.getTitle() != null ? s.getTitle().toLowerCase() : "";

                if (!name.contains(k)) {
                    matchKeyword = false;
                }
            }

            if (matchKeyword) {
                filteredList.add(s);
            }
        }

        // ===== PAGINATION =====
        int pageSize = 3;
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

        List<News> pagedList = new java.util.ArrayList<>();

        if (totalRecord > 0) {
            pagedList = filteredList.subList(start, end);
        }
        request.setAttribute("CategoryList", list);
        request.setAttribute("newList", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("top3List", top3List);
        request.getRequestDispatcher("/WEB-INF/views/common/NewsPage.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
