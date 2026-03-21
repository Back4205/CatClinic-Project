package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReceptionCancelController", urlPatterns = {"/reception-cancel"})
public class ReceptionCancelController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        if (idRaw != null) {
            int bookingID = Integer.parseInt(idRaw);
            BookingDAO dao = new BookingDAO();
            BookingHistoryDTO booking = dao.getBookingDetailByID(bookingID);

            if (booking != null) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("/WEB-INF/views/reception/reception-cancel.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect("reception-dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            int slotID = Integer.parseInt(request.getParameter("slotID"));
            String cancelReason = request.getParameter("cancelReason");

            String bankName = request.getParameter("bankName");
            String accNum = request.getParameter("accNum");
            String accName = request.getParameter("accName");

            String refundNote = "RECEPTIONIST_CANCEL | Reason: " + cancelReason
                    + " | Refund: " + bankName + " - " + accNum + " - " + accName;

            BookingDAO dao = new BookingDAO();
            boolean success = dao.cancelAndRequestRefund(bookingID, refundNote, slotID);

            if (success) {
                response.sendRedirect("reception-cancel?step=success&id=" + bookingID);
            } else {
                response.sendRedirect("reception-booking-detail?id=" + bookingID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reception-dashboard");
        }
    }
}