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
        List<VeterinarianDTO> list = dao.getActiveVeterinarians();
        List<Category> clist = cdao.getAllCategory();
        request.setAttribute("CategoryList", clist);
        request.setAttribute("vetList", list);
        request.getRequestDispatcher("/WEB-INF/views/common/ViewAllVeterinarian.jsp")
               .forward(request, response);
    }
}