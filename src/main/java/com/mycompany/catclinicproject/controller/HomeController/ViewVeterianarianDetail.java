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
@WebServlet(name="ViewVeterianarianDetail", urlPatterns={"/ViewVeterianarianDetail"})
public class ViewVeterianarianDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        int vetID = Integer.parseInt(id);
        VeterinarianDAO dao = new VeterinarianDAO();
        CategoryDao cdao = new CategoryDao();
        List<Category> clist = cdao.getAllCategory();
        request.setAttribute("CategoryList", clist);
        VeterinarianDTO v = dao.getVetProfileByVetID(vetID);
        request.setAttribute("v", v);
        request.getRequestDispatcher("/WEB-INF/views/common/ViewVetDetail.jsp")
                .forward(request, response);
    }
}
