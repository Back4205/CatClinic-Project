/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.HomeController;

import com.mycompany.catclinicproject.dao.CategoryDao;
import com.mycompany.catclinicproject.dao.FeebackDAO;
import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.dao.homeDao.NewsDao;
import com.mycompany.catclinicproject.dao.homeDao.ServiceDaoo;
import com.mycompany.catclinicproject.model.Category;
import com.mycompany.catclinicproject.model.FeedbackDTO;
import com.mycompany.catclinicproject.model.NewsDTO;
import com.mycompany.catclinicproject.model.ServiceDTO;
import com.mycompany.catclinicproject.model.ServiceFeedback;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;

/**
 *
 * @author Son
 */
@WebServlet("/loadinfo")
public class loadHomePage extends HttpServlet {

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
            out.println("<title>Servlet loadHomePage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet loadHomePage at " + request.getContextPath() + "</h1>");
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
        CategoryDao cdao = new CategoryDao();
        NewsDao ndao = new NewsDao();
        List<Category> list = cdao.getAllCategory();
        ServiceDaoo sdao = new ServiceDaoo();
        List<ServiceDTO> lists = sdao.getAllServices();
        List<NewsDTO> listn = ndao.getNews();
        FeebackDAO fdao = new FeebackDAO();
        List<FeedbackDTO> listf = fdao.getAllFeedback();
        List<ServiceFeedback> listsf = fdao.getAllBookingServices();
        request.setAttribute("listfeedback", listf);
        request.setAttribute("listnameservice", listsf);
        request.setAttribute("NewsList", listn);
        request.setAttribute("listService", lists);
        request.setAttribute("CategoryList", list);
        request.getRequestDispatcher("/WEB-INF/views/common/homePage.jsp").forward(request, response);

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
