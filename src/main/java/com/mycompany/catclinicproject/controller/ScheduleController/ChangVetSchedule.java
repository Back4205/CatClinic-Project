/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.ScheduleController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.dao.TimeSlotDAO;
import com.mycompany.catclinicproject.dao.VeterinarianDAO;
import com.mycompany.catclinicproject.model.TimeSlot2DTO;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.VeteNameID;
import com.mycompany.catclinicproject.sendmail.SendMail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Son
 */
@WebServlet(name = "ChangVetSchedule", urlPatterns = {"/changvetschedule"})
public class ChangVetSchedule extends HttpServlet {

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
            out.println("<title>Servlet ChangVetSchedule</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangVetSchedule at " + request.getContextPath() + "</h1>");
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
         HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        if (user == null || user.getRoleID() != 1) {
            response.sendRedirect(request.getContextPath() + "/login?from=booking");
            return;
        }
        TimeSlotDAO tdao = new TimeSlotDAO();
        String VetID = request.getParameter("VetID");
        String type = request.getParameter("requestType");
        List<TimeSlot2DTO> slots = tdao.getAllTimeSlots2();
        VeterinarianDAO vdao = new VeterinarianDAO();
        List<VeteNameID> listVet = vdao.getAllVets();
        request.setAttribute("listvet", listVet);
        request.setAttribute("requestType", type);
        request.setAttribute("slots", slots);
        session.setAttribute("VetID", VetID);
        request.getRequestDispatcher("WEB-INF/views/manager/changeschedule.jsp").forward(request, response);
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
        String vetIDstr = request.getParameter("VetID");
        int VetID = Integer.parseInt(vetIDstr);
        String type = request.getParameter("requestType");
        TimeSlotDAO tdao = new TimeSlotDAO();
        BookingDAO dao = new BookingDAO();
        if (type.equalsIgnoreCase("single")) {
            String dateStr = request.getParameter("requestDate");
            Date date = Date.valueOf(dateStr);
            Date today = new Date(System.currentTimeMillis());
            if (date.before(today)) {
                session.setAttribute("toast-messenger", "Phải chọn ngày sau hoặc bằng hôm nay!");
                response.sendRedirect("changvetschedule?VetID=" + VetID);
                return;
            }
            String[] slots = request.getParameterValues("slot");
            List<Integer> listBooked = tdao.getBookedSlotsInLeave(VetID, date, slots);
            for (Integer slotID : listBooked) {
                int newVet = tdao.getVetForBooking(slotID, date);
                if (newVet != -1 && newVet != VetID) {
                    int bookingID = tdao.getBookingID(VetID, slotID, date);
                    tdao.updateVetByBookingID(bookingID, newVet);
                    tdao.upsertBooked(newVet, slotID, date);
                } else {
                    int bookingID = tdao.getBookingID(VetID, slotID, date);
                    sendCancelMail(bookingID, date);
                    dao.updateStatusPendingCancelRefund(bookingID);
                }
                tdao.updateAbsent(VetID, slotID, date);
            }
            if (slots != null) {
                for (String s : slots) {
                    int slotID = Integer.parseInt(s);
                    tdao.insertAbsent(VetID, slotID, date);
                }
            }
        } else {
            int slotss = tdao.getTotalSlots() + 1;
            Date fromDate = Date.valueOf(request.getParameter("fromDate"));
            Date toDate = Date.valueOf(request.getParameter("toDate"));
            Date today = new Date(System.currentTimeMillis());

            if (!fromDate.after(today)) {
                session.setAttribute("toast-messenger", "Phải chọn ngày bằng hoặc sau hôm nay!");
                response.sendRedirect("changvetschedule?VetID=" + VetID);
                return;
            } else if (!fromDate.before(toDate)) {
                session.setAttribute("toast-messenger", "Phải chọn ngày kết thúc sau ngày bắt đầu!");
                response.sendRedirect("changvetschedule?VetID=" + VetID);
                return;
            }
            long oneDay = 24 * 60 * 60 * 1000;
            for (long d = fromDate.getTime(); d <= toDate.getTime(); d += oneDay) {
                Date currentDate = new Date(d);
                List<Integer> listBooked = tdao.getBookedSlots(VetID, currentDate);
                for (Integer slotID : listBooked) {
                    int newVet = tdao.getVetForBooking(slotID, currentDate);
                    if (newVet != -1 && newVet != VetID) {
                        int bookingID = tdao.getBookingID(VetID, slotID, currentDate);
                        tdao.updateVetByBookingID(bookingID, newVet);
                        tdao.upsertBooked(newVet, slotID, currentDate);
                        tdao.updateAbsent(VetID, slotID, currentDate);
                    } else {
                        int bookingID = tdao.getBookingID(VetID, slotID, currentDate);
                        sendCancelMail(bookingID, currentDate);
                        dao.updateStatusPendingCancelRefund(bookingID);
                    }
                    tdao.updateAbsent(VetID, slotID, currentDate);
                }
                for (int i = 1; i < slotss; i++) {
                    tdao.insertAbsent(VetID, i, currentDate);
                }
            }
            session.setAttribute("toast-messenger-success", "Đã hủy các lịch nghỉ đã chọn!");

        }
        response.sendRedirect("changvetschedule");
    }

    public void sendCancelMail(int bookingID, Date date) {
        TimeSlotDAO dao = new TimeSlotDAO();
        String email = dao.getEmailByBookingID(bookingID);
        String link = "http://localhost:8080/booking-detail?id=" + bookingID;

        String message
                = "<h3>Cat Clinic Notification</h3>"
                + "<p>Your appointment has been cancelled. The doctor had an unexpected commitment, we sincerely apologize for this, but we will issue a refund..\n Please check Detail booking</p>"
                + "<p>Booking ID: " + bookingID + "</p>"
                + "<p>Date: " + date + "</p>"
                + "<br>";

        SendMail.send(email, "Appointment Cancelled", message);
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
