package com.mycompany.catclinicproject.controller.receptionistController;

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

@WebServlet(name = "ReceptionCancelController", urlPatterns = {"/reception-cancel"})
public class ReceptionCancelController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String idRaw = request.getParameter("id");
        if (idRaw != null) {
            BookingDAO dao = new BookingDAO();
            int bookingID = Integer.parseInt(idRaw);
            int  k = dao.hasValidTotalAmount(bookingID);
            if (k == 0) {
                dao.cancelBooking(bookingID);
                session.setAttribute("toastMessage","cancel-success!" );
                response.sendRedirect("appointmentdetail?id="+bookingID);
                return;
            } else {
                BookingHistoryDTO booking = dao.getBookingDetailByID(bookingID);

                if (booking != null) {
                    request.setAttribute("booking", booking);
                    request.getRequestDispatcher("/WEB-INF/views/reception/reception-cancel.jsp").forward(request, response);
                    return;
                }
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
