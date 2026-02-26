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
        CategoryDao sdao = new CategoryDao();
        List<Category> categoryList = sdao.getCategory();
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("WEB-INF/views/manager/categoryList.jsp").forward(request, response);
    }
}
