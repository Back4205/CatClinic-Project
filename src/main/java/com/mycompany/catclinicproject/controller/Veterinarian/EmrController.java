/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.homeDao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.model.EMRDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Son
 */
@WebServlet(name = "EmrController", urlPatterns = {"/EmrController"})
public class EmrController extends HttpServlet {

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
            out.println("<title>Servlet EmrController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EmrController at " + request.getContextPath() + "</h1>");
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
        String idParam = request.getParameter("medicalRecordID");

        try {
            
            int medicalRecordID = Integer.parseInt(idParam);
            BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
            EMRDTO emr = dao.getEMRDetail(medicalRecordID);
            int catID = dao.getCatIDByMedicalRecordID(medicalRecordID);
            request.setAttribute("emr", emr);
            request.setAttribute("activePage", "assigned");
            request.setAttribute("catId", catID);

            request.getRequestDispatcher("WEB-INF/views/veterinarian/medicalrecord.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("assignCase");
        }

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
        int medicalRecordID = Integer.parseInt(request.getParameter("medicalRecordID"));
        String diagnosis = request.getParameter("diagnosis");
        String symptoms = request.getParameter("symptoms");
        String treatmentPlan = request.getParameter("treatmentPlan");
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        dao.updateMedicalRecord(medicalRecordID, diagnosis, symptoms, treatmentPlan);
        response.sendRedirect("/xray?medicalRecordID=" + medicalRecordID);
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
