/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.ScheduleController;

import com.mycompany.catclinicproject.dao.TimeSlotDAO;
import com.mycompany.catclinicproject.dao.VeterinarianDAO;
import com.mycompany.catclinicproject.model.TimeSlot2DTO;

import com.mycompany.catclinicproject.model.VetScheduleDTO;
import com.mycompany.catclinicproject.model.VeteNameID;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "ViewSchedule", urlPatterns = {"/viewschedule"})
public class ViewSchedule extends HttpServlet {

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
            out.println("<title>Servlet ViewSchedule</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewSchedule at " + request.getContextPath() + "</h1>");
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
        int currentYear = LocalDate.now().getYear();
        int year = request.getParameter("year") != null
                ? Integer.parseInt(request.getParameter("year"))
                : currentYear;
        List<Integer> years = new ArrayList<>();
        for (int i = currentYear - 10; i <= currentYear + 10; i++) {
            years.add(i);
        }
        List<String> weeks = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        LocalDate start = LocalDate.of(year, 1, 1).with(DayOfWeek.MONDAY);

        while (start.getYear() <= year) {
            LocalDate end = start.plusDays(6);
            weeks.add(start.format(formatter) + " To " + end.format(formatter));
            start = start.plusWeeks(1);
            if (start.getYear() > year && start.getMonthValue() > 1) {
                break;
            }
        }
        LocalDate now = LocalDate.now();
        LocalDate thisWeekStart = now.with(DayOfWeek.MONDAY);
        LocalDate thisWeekEnd = thisWeekStart.plusDays(6);
        String thisWeek = thisWeekStart.format(formatter)
                + " To "
                + thisWeekEnd.format(formatter);
        String week = request.getParameter("week");
        if (week == null) {
            week = thisWeek;
        }
        String startDateStr = week.split(" To ")[0];
        LocalDate startDate = LocalDate.parse(
                startDateStr + "/" + year,
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
        LocalDate endDate = startDate.plusDays(6);
        List<LocalDate> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(startDate.plusDays(i));
        }
        TimeSlotDAO tdao = new TimeSlotDAO();
        String VetID = request.getParameter("VetID");
        int vetID;
        if (VetID != null) {
            vetID = Integer.parseInt(VetID);
        } 
        else {
            vetID = 1;
        }
        VeterinarianDAO vdao = new VeterinarianDAO();
        List<VeteNameID> listVet = vdao.getAllVets();
        List<VetScheduleDTO> schedule = tdao.getVetSchedule(vetID, startDate, endDate);
        List<TimeSlot2DTO> slots = tdao.getAllTimeSlots2();
        request.setAttribute("schedule", schedule);
        request.setAttribute("years", years);
        request.setAttribute("weeks", weeks);
        request.setAttribute("days", days);
        request.setAttribute("year", year);
        request.setAttribute("thisWeek", thisWeek);
        request.setAttribute("week", week);
        request.setAttribute("slots", slots);
        request.setAttribute("listVet", listVet);
        request.setAttribute("VetID", vetID);
        request.getRequestDispatcher("/WEB-INF/views/manager/ScheduleView.jsp")
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
