/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.NewsManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.NewDAO;
import com.mycompany.catclinicproject.model.News;
import com.mycompany.catclinicproject.model.NewsImages;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="CreateNew", urlPatterns={"/CreateNew"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class CreateNew extends HttpServlet {

      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/manager/newCreate.jsp")
                .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    Part filePart = request.getPart("image");
    String fileName1 = "new_" + System.currentTimeMillis();
    String bannerUrl = CloudinaryUntil.uploadImage(filePart, fileName1,"my_new");
    String title = request.getParameter("title");
    String description = request.getParameter("description");
    boolean isActive = request.getParameter("isActive") != null;

    NewDAO dao = new NewDAO();
    News n = new News();
    n.setBanner(bannerUrl);
    n.setTitle(title);
    n.setDescription(description);
    n.setIsActive(isActive);
    // 🔥 1. Insert News
    int newID = dao.insertNews(n);

    // 🔥 2. Upload nhiều ảnh
    for (Part part : request.getParts()) {
        if (part.getName().equals("images") && part.getSize() > 0) {
            String fileName2 = "new_" + System.currentTimeMillis();
            String imgUrl = CloudinaryUntil.uploadImage(part, fileName2,"my_new");
            NewsImages ni = new NewsImages();
                    ni.setNewId(newID);
                    ni.setImgUrl(imgUrl);
                    dao.createImage(ni);
        }
    }

    response.sendRedirect(request.getContextPath() + "/ViewNewList");
}
}
