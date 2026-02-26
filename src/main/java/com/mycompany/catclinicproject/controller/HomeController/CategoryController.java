/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.HomeController;

import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.dao.homeDao.ServiceDaoo;
import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.ServiceDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "CategoryController", urlPatterns = {"/CategoryController"})
public class CategoryController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CategoryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cidParam = request.getParameter("id");
        if (cidParam == null) {
            response.sendRedirect("home");
            return;
        }
        int cid = Integer.parseInt(cidParam);
        int page = 1;
        int pageSize = 6;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        CategoryDao cdao = new CategoryDao();
        List<Category> list = cdao.getAllCategory();
        CategoryDao categoryDao = new CategoryDao();
        ServiceDaoo serviceDaoo = new ServiceDaoo();
        Category category = categoryDao.getCategoryById(cid);
        List<ServiceDTO> services = serviceDaoo.getServiceByCategory(cid, page, pageSize);
        int total = serviceDaoo.countServiceByCategory(cid);

        int totalPage = total / pageSize;
        if (total % pageSize != 0) {
            totalPage++;
        }
        request.setAttribute("CategoryList", list);
        request.setAttribute("category", category);
        request.setAttribute("services", services);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("cid", cid);
        request.getRequestDispatcher("/WEB-INF/views/common/CategoryPage.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
