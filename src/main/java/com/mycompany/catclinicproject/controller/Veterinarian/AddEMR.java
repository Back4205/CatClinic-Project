/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.dao.MedicalRecordDAO;
import com.mycompany.catclinicproject.dao.NotificationDAO;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.websocket.NotificationSocket;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Son
 */
@WebServlet(name = "AddEMR", urlPatterns = {"/addemr"})
public class AddEMR extends HttpServlet {

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
            out.println("<title>Servlet AddEMR</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddEMR at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        NotificationDAO daon = new NotificationDAO();
        User acc = (User) session.getAttribute("acc");
        int UserID = acc.getUserID();
        int VetID = daon.getVetIDByUserID(UserID);
        int bookingID = Integer.parseInt(request.getParameter("bookingID"));
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        boolean checkstatus = dao.isAnyInTreatment(VetID);
        MedicalRecordDAO mdao = new MedicalRecordDAO();
        int nextid = mdao.getGeneralCheckBookingId(VetID);
        int id = mdao.getMedicalRecordIdByBookingId(bookingID);
        if (id != -1) {
            response.sendRedirect("EmrController?medicalRecordID=" + id);
        } else {
            if (checkstatus) {
                session.setAttribute("toastMessage", "Đang có ca khám khác chưa hoàn thành nên chưa thể thêm mới.");
                response.sendRedirect("DashboardController");
                return;
            }
             if (bookingID != nextid) {
                session.setAttribute("toastMessage", "Phải khám theo thứ tự booking.");
                response.sendRedirect("DashboardController");
                return;
            }
             
             
            
            boolean created = dao.createMedicalRecord(bookingID);
            if (created) {
                int medicalRecordID = dao.getMedicalRecordIDByBookingID(bookingID);
                dao.updateStatusToInTreatment(medicalRecordID);
                String message = "You have just updated a medical record.";
                int notiID = daon.createNotification(VetID, message, medicalRecordID, "EMR");
                if (notiID != -1) {
                    NotificationSocket.sendNotification(VetID, notiID, message, "EMR");
                }
                response.sendRedirect("EmrController?medicalRecordID=" + medicalRecordID);
            } else {
                session.setAttribute("toastMessage", "Tạo medical record thất bại.");
                response.sendRedirect("DashboardController");
            }
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
