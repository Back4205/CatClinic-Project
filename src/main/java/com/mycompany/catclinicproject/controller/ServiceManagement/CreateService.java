/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.Service;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "CreateService", urlPatterns = {"/CreateService"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CreateService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDao cdao = new CategoryDao();
        String categoryID = request.getParameter("categoryID");
        request.setAttribute("categoryList", cdao.getAllCategory());
        request.setAttribute("categoryID", categoryID);
        request.getRequestDispatcher("/WEB-INF/views/manager/serviceCreate.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name").trim();
        String priceRaw = request.getParameter("price");
        String description = request.getParameter("description").trim();
        String timeRaw = request.getParameter("time");
        String categoryRaw = request.getParameter("categoryID");
        boolean isActive = request.getParameter("isActive") != null;

        String error = null;

        // ===== VALIDATE NAME =====
        if (name == null || name.trim().isEmpty()) {
            error = "Service name cannot be empty!";
        } else if (name.length() > 100) {
            error = "Service name must be less than 100 characters!";
        }

        // ===== VALIDATE PRICE =====
        double price = 0;
        if (error == null) {
            try {
                price = Double.parseDouble(priceRaw);
                if (price <= 0) {
                    error = "Price must be greater than 0!";
                }
            } catch (NumberFormatException e) {
                error = "Invalid price format!";
            }
        }

        // ===== VALIDATE TIME =====
        int time = 0;
        if (error == null) {
            try {
                time = Integer.parseInt(timeRaw);
                if (time <= 0 || time > 600) {
                    error = "Time must be between 1 and 600 minutes!";
                }
            } catch (NumberFormatException e) {
                error = "Invalid time format!";
            }
        }

        // ===== VALIDATE CATEGORY =====
        int categoryID = 0;
        if (error == null) {
            try {
                categoryID = Integer.parseInt(categoryRaw);
            } catch (NumberFormatException e) {
                error = "Please select a valid category!";
            }
        }

        // ===== VALIDATE IMAGE =====
        Part filePart = request.getPart("image");
        if (error == null && filePart != null && filePart.getSize() > 0) {
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                error = "File must be an image!";
            }
        }

        // ===== CHECK SERVICE NAME DUPLICATE =====
        ServiceDAO dao = new ServiceDAO();
        if (error == null && dao.isServiceNameExists(name)) {
            error = "Service name already exists!";
        }

        // ===== IF ERROR → RETURN BACK =====
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("categoryID", categoryID);
            request.setAttribute("categoryList", new CategoryDao().getAllCategory());
            request.getRequestDispatcher("/WEB-INF/views/manager/serviceCreate.jsp")
                    .forward(request, response);
            return;
        }

        // ===== UPLOAD IMAGE =====
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = "service_" + System.currentTimeMillis();
            imageUrl = CloudinaryUntil.uploadImage(filePart, fileName);
        }

        // ===== CREATE SERVICE =====
        Service s = new Service();
        s.setNameService(name.trim());
        s.setPrice(price);
        s.setDescription(description);
        s.setTimeService(time);
        s.setIsActive(isActive);
        s.setCategoryID(categoryID);
        s.setImgUrl(imageUrl);

        dao.insertService(s);

        response.sendRedirect(request.getContextPath()
                + "/ViewServiceList?id=" + categoryID);
    }
}
