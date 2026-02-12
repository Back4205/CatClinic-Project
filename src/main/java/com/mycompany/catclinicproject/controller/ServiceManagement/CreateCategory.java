/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.CategoryDAO;
import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.Service;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="CreateCategory", urlPatterns={"/CreateCategory"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CreateCategory extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/manager/categoryCreate.jsp")
               .forward(request, response);
    } 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String categoryName = request.getParameter("name");
        Part filePart = request.getPart("image");
        String fileName = "service_" + System.currentTimeMillis();
        String imageUrl = CloudinaryUntil.uploadImage(filePart, fileName);
        String description = request.getParameter("description");
        boolean isActive = request.getParameter("isActive") != null;
        Category s = new Category();
        s.setCategoryName(categoryName);
        s.setBanner(imageUrl);
        s.setDescription(description);
        s.setActive(isActive);
        CategoryDAO cdao = new CategoryDAO();
        cdao.insertCategory(s);
        response.sendRedirect(request.getContextPath() + "/ViewCategoryList");
    }

}
