/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.NewsManagement;

import com.mycompany.catclinicproject.dao.NewDAO;
import com.mycompany.catclinicproject.model.News;
import com.mycompany.catclinicproject.model.NewsImages;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="ChangeStatus", urlPatterns={"/ChangeStatus"})
public class ChangeStatus extends HttpServlet {
   

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        NewDAO dao = new NewDAO();
        News n = dao.getNewsById(id);
        
        if (n.isIsActive()) {
            dao.updateStatus(id, false);
            response.sendRedirect("ViewNewList");
        } else {
            List<NewsImages> images = dao.getNewsImagesByNewsId(id);
            String processedContent = n.getDescription();
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    String placeholder = "[IMG" + (i + 1) + "]";
                    String imgHtml = "<div style='text-align:center; margin:10px;'><img src='" 
                                     + images.get(i).getImgUrl() + "' style='max-width:100%;'></div>";
                    processedContent = processedContent.replace(placeholder, imgHtml);
                }
            }
            
            request.setAttribute("news", n);
            request.setAttribute("content", processedContent);
            request.getRequestDispatcher("WEB-INF/views/manager/newPreview.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }
}
