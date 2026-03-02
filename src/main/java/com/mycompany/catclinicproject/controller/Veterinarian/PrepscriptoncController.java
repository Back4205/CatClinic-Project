/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.homeDao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.dao.homeDao.DrugDAO;
import com.mycompany.catclinicproject.model.DrugDTO;
import com.mycompany.catclinicproject.model.EMRDTO;
import com.mycompany.catclinicproject.model.PrescriptionView;
import com.mycompany.catclinicproject.model.TestOrderDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "PrepscriptoncController", urlPatterns = {"/preController"})
public class PrepscriptoncController extends HttpServlet {

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
            out.println("<title>Servlet PrepscriptoncController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PrepscriptoncController at " + request.getContextPath() + "</h1>");
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
        int medicalRecordID = Integer.parseInt(request.getParameter("medicalRecordID"));
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        EMRDTO emr = dao.getEMRDetail(medicalRecordID);
        request.setAttribute("emr", emr);
        DrugDAO daod = new DrugDAO();

        List<DrugDTO> drugList = daod.getAllDrug();
        List<PrescriptionView> preList = daod.getPrescriptionByMedicalRecordID(medicalRecordID);
        request.setAttribute("drugList", drugList);
        request.setAttribute("prescriptionList", preList);
        request.setAttribute("medicalRecordID", medicalRecordID);
    request.setAttribute("activePage", "assigned");

        request.getRequestDispatcher("WEB-INF/views/veterinarian/prescription.jsp")
                .forward(request, response);
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
        String advice = request.getParameter("advice");
        DrugDAO dao = new DrugDAO();
        Integer prescriptionID
                = dao.getPrescriptionIDByMedicalRecordID(medicalRecordID);
        if (prescriptionID != null) {
            dao.deletePrescriptionDrugByPrescriptionID(prescriptionID);
            dao.deletePrescriptionByMedicalRecordID(medicalRecordID);
        }
        // 1️⃣ Tạo Prescription trước
        int prescriptionIDs = dao.createPrescription(medicalRecordID, advice);
        if (prescriptionIDs == 0) {
            response.getWriter().println("Create Prescription failed!");
            return;
        }

        String[] drugIDs = request.getParameterValues("drugID");

        if (drugIDs != null) {

            for (String dID : drugIDs) {
                response.getWriter().println("rong roi!" + dID);

                int drugID = Integer.parseInt(dID);

                String qRaw = request.getParameter("quantity_" + drugID);

                if (qRaw == null || qRaw.isEmpty()) {
                    continue;
                }

                int quantity = Integer.parseInt(qRaw);

                String instruction
                        = request.getParameter("instruction_" + drugID);

                dao.insertPrescriptionDrug(
                        prescriptionIDs,
                        drugID,
                        quantity,
                        instruction
                );
            }
        } else {
            response.getWriter().println("rong roi!");
            return;
        }

        // 👉 redirect lại EMR
        response.sendRedirect("preController?medicalRecordID=" + medicalRecordID);

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
