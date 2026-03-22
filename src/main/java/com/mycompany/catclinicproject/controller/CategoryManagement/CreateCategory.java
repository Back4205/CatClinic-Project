package com.mycompany.catclinicproject.controller.CategoryManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.model.Category;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "CreateCategory", urlPatterns = {"/CreateCategory"})
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

    private String escapeHtml(String input) {
    return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDao cdao = new CategoryDao();

        String categoryName = request.getParameter("name");
        String description = request.getParameter("description");
        boolean isActive = request.getParameter("isActive") != null;

        // Clean HTML tags
        categoryName = escapeHtml(categoryName);
        description = escapeHtml(description);

        if (categoryName != null) {
            categoryName = categoryName.trim();
        }

        // Validate empty
        if (categoryName == null || categoryName.isEmpty()) {
            request.setAttribute("error", "Category name cannot be empty!");
            request.getRequestDispatcher("/WEB-INF/views/manager/categoryCreate.jsp")
                    .forward(request, response);
            return;
        }

        // Validate multiple spaces
        if (categoryName.matches(".*\\s{2,}.*")) {
            request.setAttribute("error", "Category name cannot contain multiple spaces!");
            request.getRequestDispatcher("/WEB-INF/views/manager/categoryCreate.jsp")
                    .forward(request, response);
            return;
        }

        // Check duplicate name
        if (cdao.isCategoryNameExists(categoryName)) {
            request.setAttribute("error", "Category name already exists!");
            request.getRequestDispatcher("/WEB-INF/views/manager/categoryCreate.jsp")
                    .forward(request, response);
            return;
        }

        // Upload image
        Part filePart = request.getPart("image");
        String imageUrl = null;

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = "category_" + System.currentTimeMillis();
            imageUrl = CloudinaryUntil.uploadImage(filePart, fileName,"my_category");
        }

        // Create category object
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setBanner(imageUrl);
        category.setDescription(description);
        category.setActive(isActive);

        // Insert database
        cdao.insertCategory(category);

        response.sendRedirect(request.getContextPath() + "/ViewCategoryList");
    }
}