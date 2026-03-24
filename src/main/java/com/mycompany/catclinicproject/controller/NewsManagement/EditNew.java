/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.NewsManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.NewDAO;
import com.mycompany.catclinicproject.model.News;
import com.mycompany.catclinicproject.model.NewsImages;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="EditNew", urlPatterns={"/EditNew"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class EditNew extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int newsId = Integer.parseInt(request.getParameter("newsId"));
        NewDAO ndao = new NewDAO();
        News news = ndao.getNewsById(newsId);
        List<NewsImages> imgList = ndao.getNewsImagesByNewsId(newsId);
        request.setAttribute("news", news);
    request.setAttribute("imageList", imgList);
        request.getRequestDispatcher("/WEB-INF/views/manager/newEdit.jsp")
                .forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    int newsId = Integer.parseInt(request.getParameter("newsId"));
    NewDAO newsDAO = new NewDAO();
    News n = new News();
    Part bannerPart = request.getPart("bannerFile");
        if (bannerPart != null && bannerPart.getSize() > 0) {
            String bannerUrl = CloudinaryUntil.uploadImage(bannerPart, "banner_" + System.currentTimeMillis(),"my_new");
            n.setBanner(bannerUrl);
        }
    String title = request.getParameter("title");
    String description = request.getParameter("description");
    if (title != null) {
            title = title.trim();
        }

        if (description != null) {
            description = description.trim();
        }
        if (title == null || title.isEmpty()) {
            request.setAttribute("error", "Title must not be empty!");
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.getRequestDispatcher("/WEB-INF/views/manager/newEdit.jsp").forward(request, response);
            return;
        }
        if (description == null || description.isEmpty()) {
            request.setAttribute("error", "Description must not be empty!");
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.getRequestDispatcher("/WEB-INF/views/manager/newEdit.jsp").forward(request, response);
            return;
        }
    boolean isActive = request.getParameter("isActive") != null;   
    n.setTitle(title);
    n.setDescription(description);
    n.setIsActive(isActive);
    newsDAO.updateNews(n,newsId);
    boolean hasNewImages = false;

    for (Part part : request.getParts()) {
        if (part.getName().equals("images") && part.getSize() > 0) {
            hasNewImages = true;
            break;
        }
    }

    if (hasNewImages) {
        newsDAO.deleteByNewsId(newsId);
    }
    for (Part part : request.getParts()) {
        if (part.getName().equals("images") && part.getSize() > 0) {
            String fileName = "new_" + System.currentTimeMillis();
            String imgUrl = CloudinaryUntil.uploadImage(part, fileName,"my_new");
            NewsImages ni = new NewsImages();
                    ni.setNewId(newsId);
                    ni.setImgUrl(imgUrl);
                    newsDAO.createImage(ni);
        }
    }
    response.sendRedirect(request.getContextPath() + "/ViewNewList");
    }


}
