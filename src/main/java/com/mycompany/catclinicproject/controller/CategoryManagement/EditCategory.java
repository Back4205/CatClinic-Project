/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.CategoryManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.CategoryDAO;
import com.mycompany.catclinicproject.model.Category;
import java.io.IOException;
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
@WebServlet(name="EditCategory", urlPatterns={"/EditCategory"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class EditCategory extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        CategoryDAO cdao = new CategoryDAO();
        Category categoryList = cdao.getCategoryById(id);

        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/views/manager/categoryEdit.jsp")
                .forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int categoryID = Integer.parseInt(request.getParameter("id"));
        String categoryName = request.getParameter("name");
        Part filePart = request.getPart("image");
        String imageUrl;

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = "service_" + System.currentTimeMillis();
            imageUrl = CloudinaryUntil.uploadImage(filePart, fileName);
        } else {
            imageUrl = request.getParameter("oldimage");
        }

        String description = request.getParameter("description");
        Category c = new Category();
        c.setCategoryID(categoryID);
        c.setCategoryName(categoryName);
        c.setBanner(imageUrl);
        c.setDescription(description);
        CategoryDAO cdao = new CategoryDAO();
        cdao.updateCategory(c);
       response.sendRedirect(request.getContextPath() + "/ViewCategoryList");
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
