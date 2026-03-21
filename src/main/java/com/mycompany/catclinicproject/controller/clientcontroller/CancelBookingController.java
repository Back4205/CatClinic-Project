
package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CancelBookingController", urlPatterns = {"/cancel-booking"})
public class CancelBookingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("acc") : null);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idRaw = request.getParameter("id");
        if (idRaw != null) {
            int bookingID = Integer.parseInt(idRaw);
            BookingDAO dao = new BookingDAO();
            BookingHistoryDTO booking = dao.getBookingDetailByID(bookingID);

            if (booking != null) {
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("/WEB-INF/views/client/cancel-booking.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect("booking-history");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int bookingID = Integer.parseInt(request.getParameter("bookingID"));
        int slotID = Integer.parseInt(request.getParameter("slotID"));

        String cancelReason = request.getParameter("cancelReason");

        String bankName = request.getParameter("bankName");
        String accNum = request.getParameter("accNum");
        String accName = request.getParameter("accName");

        String refundNote = "Reason: " + cancelReason + " | Refund Request: " + bankName + " - " + accNum + " - " + accName;

        BookingDAO dao = new BookingDAO();
        boolean success = dao.cancelAndRequestRefund(bookingID, refundNote, slotID);

        if (success) {
            response.sendRedirect("cancel-booking?step=success&id=" + bookingID);
        } else {
            response.sendRedirect("booking-detail?id=" + bookingID);
        }
    }
}