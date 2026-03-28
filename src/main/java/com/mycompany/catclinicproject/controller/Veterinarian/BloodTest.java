/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.dao.MedicalRecordDAO;
import com.mycompany.catclinicproject.dao.NotificationDAO;
import com.mycompany.catclinicproject.dao.ServiceDAO;
import com.mycompany.catclinicproject.model.EMRDTO;
import com.mycompany.catclinicproject.model.TestOrderDTO;
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
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "Board", urlPatterns = {"/bloodtest"})
public class BloodTest extends HttpServlet {

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
            out.println("<title>Servlet Board</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Board at " + request.getContextPath() + "</h1>");
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
           request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;
        if (user == null || user.getRoleID() != 2) {
            response.sendRedirect(request.getContextPath() + "/login?from=booking");
            return;
        }
        int medicalRecordID = Integer.parseInt(request.getParameter("medicalRecordID"));
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        EMRDTO emr = dao.getEMRDetail(medicalRecordID);
        List<TestOrderDTO> testOrders
                = dao.getTestOrdersByMedicalRecordIDBloodTest(medicalRecordID);
        request.setAttribute("emr", emr);
        request.setAttribute("testOrders", testOrders);
            request.setAttribute("activePage", "assigned");

        request.getRequestDispatcher("WEB-INF/views/veterinarian/bloodtest.jsp")
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
        HttpSession session = request.getSession();
        NotificationDAO ndao = new NotificationDAO();
        String idParam = request.getParameter("medicalRecordID");
                String resultName = request.getParameter("resultName");
        int medicalRecordID = Integer.parseInt(idParam);
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        MedicalRecordDAO mdao = new MedicalRecordDAO();
        mdao.updateStatusToWaiting(medicalRecordID);
        boolean k = dao.insertTestOrderBloodTest(medicalRecordID,resultName);
        User acc = (User) session.getAttribute("acc");
        int UserID = acc.getUserID();
        int VetID = ndao.getVetIDByUserID(UserID);
         ServiceDAO svdao =  new ServiceDAO();
        int bookingID = svdao.getBookingIDByMedicalRecordID(medicalRecordID);
        svdao.addServiceToBooking(bookingID, 7);
        if(k == true){
           String message = "You have just create a X-ray request.";
                int notiID = ndao.createNotification(VetID, message, medicalRecordID, "request BL");
                if (notiID != -1) {
                    NotificationSocket.sendNotification(VetID, notiID, message, "request BL");
                }
        }
        response.sendRedirect("bloodtest?medicalRecordID=" + medicalRecordID);
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
