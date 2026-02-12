/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.ServiceManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.CategoryDAO;
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
        CategoryDAO cdao = new CategoryDAO();
        String categoryID = request.getParameter("categoryID");
        request.setAttribute("categoryList", cdao.getAllCategory());
        request.setAttribute("categoryID", categoryID);
        request.getRequestDispatcher("/WEB-INF/views/manager/serviceCreate.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        int time = Integer.parseInt(request.getParameter("time"));
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        boolean isActive = request.getParameter("isActive") != null;
        // LẤY FILE – KHỚP name="image" trong JSP
        Part filePart = request.getPart("image");
        // TÊN FILE KHÔNG TRÙNG
        String fileName = "service_" + System.currentTimeMillis();
        // UPLOAD CLOUDINARY (DÙNG Part)
        String imageUrl = CloudinaryUntil.uploadImage(filePart, fileName);
        Service s = new Service();
        s.setNameService(name);
        s.setPrice(price);
        s.setDescription(description);
        s.setTimeService(time);
        s.setIsActive(isActive);
        s.setCategoryID(categoryID);
        s.setImgUrl(imageUrl);
        ServiceDAO dao = new ServiceDAO();
        dao.insertService(s);

        response.sendRedirect(request.getContextPath() + "/ViewServiceList?id="+categoryID);
    }
}