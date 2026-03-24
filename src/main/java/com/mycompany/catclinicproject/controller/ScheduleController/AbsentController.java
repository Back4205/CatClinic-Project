/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.ScheduleController;

import com.mycompany.catclinicproject.dao.TimeSlotDAO;
import com.mycompany.catclinicproject.model.TimeSlotDetailDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "AbsentController", urlPatterns = {"/absentController"})
public class AbsentController extends HttpServlet {

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
            out.println("<title>Servlet AbsentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AbsentController at " + request.getContextPath() + "</h1>");
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

        String requestType = request.getParameter("requestType");
        String vetID = request.getParameter("VetID");
        int VetID = Integer.parseInt(vetID);
        TimeSlotDAO dao = new TimeSlotDAO();
        List<TimeSlotDetailDTO> list = new ArrayList<>();
        if ("range".equals(requestType)) {
            String dateFromstr = request.getParameter("dateFrom");
            String dateTostr = request.getParameter("dateTo");
            Date DateFrom = Date.valueOf(dateFromstr);
            Date DateTo = Date.valueOf(dateTostr);
            list = dao.getActiveAbsentSlotsRange(VetID, DateFrom, DateTo);
            request.setAttribute("dateFrom", DateFrom);
            request.setAttribute("dateTo", DateTo);
        } else {
            String date = request.getParameter("dateSingle");
            Date sqlDate = null;
            if (date == null || date.trim().isEmpty()) {
                sqlDate = new java.sql.Date(System.currentTimeMillis());
            } else {
                sqlDate = Date.valueOf(date);
            }
            list = dao.getActiveAbsentSlots(VetID, sqlDate);
            request.setAttribute("dateSingle", sqlDate);
        }
        request.setAttribute("requestType", requestType);
        request.setAttribute("list", list);
        request.getRequestDispatcher("WEB-INF/views/manager/viewlistabsent.jsp").forward(request, response);
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
        String[] selectedIds = request.getParameterValues("selectedIds");
        String vetstr = request.getParameter("VetID");
        int vetid = Integer.parseInt(vetstr);
        TimeSlotDAO dao = new TimeSlotDAO();
        Date today = Date.valueOf(LocalDate.now());

        if (selectedIds == null || selectedIds.length == 0) {
            request.setAttribute("message", "Bạn chưa chọn lịch nghỉ nào!");
        } else {
            List<String[]> toDelete = new ArrayList<>();
            for (String value : selectedIds) {
                String[] parts = value.split("\\|");
                String vetID = parts[0];
                String slotID = parts[1];
                String date = parts[2];
                int VetID = Integer.parseInt(vetID);
                int SlotID = Integer.parseInt(slotID);
                Date currentdate = Date.valueOf(date);
                if (currentdate.before(today)) {
                    session.setAttribute("toast-messenger", "Phải chọn ngày lớn hơn hoặc bằng hôm nay!");
                    response.sendRedirect("absentController?VetID=" + vetid);
                    return;
                }
                if (currentdate.equals(today)) {
                    String status = dao.getStatus(VetID, SlotID, today);
                    if ("Booked".equalsIgnoreCase(status)) {
                        session.setAttribute("toast-messenger", "Đã có booked không thể hủy!");
                        response.sendRedirect("absentController?VetID=" + vetid);
                        return;
                    }
                }
                toDelete.add(new String[]{vetID, slotID, date});
            }

            for (String[] item : toDelete) {
                int VetID = Integer.parseInt(item[0]);
                int SlotID = Integer.parseInt(item[1]);
                Date currentdate = Date.valueOf(item[2]);
                dao.deleteTimeSlot(VetID, SlotID, currentdate);
            }

            session.setAttribute("toast-messenger-success", "Đã hủy các lịch nghỉ đã chọn!");
        }
        response.sendRedirect("absentController?VetID=" + vetid);

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
