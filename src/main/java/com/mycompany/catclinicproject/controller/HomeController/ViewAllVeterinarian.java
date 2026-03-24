/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.HomeController;

import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.dao.VeterinarianDAO;
import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.VeterinarianDTO;
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
@WebServlet(name="ViewAllVeterinarian", urlPatterns={"/ViewAllVeterinarian"})
public class ViewAllVeterinarian extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao cdao = new CategoryDao();
        VeterinarianDAO dao = new VeterinarianDAO();
        List<VeterinarianDTO> fullList = dao.getActiveVeterinarians();
        List<Category> clist = cdao.getAllCategory();
        String keyword = request.getParameter("search");
        if (keyword == null) keyword = "";

        List<VeterinarianDTO> filteredList = new java.util.ArrayList<>();

        for (VeterinarianDTO s : fullList) {

            boolean matchKeyword = true;
            boolean matchStatus = true;

            if (!keyword.trim().isEmpty()) {
                String k = keyword.toLowerCase();
                String name = s.getFullName() != null ? s.getFullName().toLowerCase() : "";

                if (!name.contains(k)) {
                    matchKeyword = false;
                }
            }
            if (matchKeyword) {
                filteredList.add(s);
            }
        }

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

        List<VeterinarianDTO> pagedList = new java.util.ArrayList<>();

        if (totalRecord > 0) {
            pagedList = filteredList.subList(start, end);
        }
                request.setAttribute("vetList", pagedList);
        request.setAttribute("currentSearch", keyword);
        request.setAttribute("CategoryList", clist);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.getRequestDispatcher("/WEB-INF/views/common/ViewAllVeterinarian.jsp")
               .forward(request, response);
    }
}