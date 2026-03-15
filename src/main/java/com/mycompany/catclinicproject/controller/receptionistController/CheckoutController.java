package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");
        String condition = request.getParameter("condition");

        try {
            if (idRaw != null) {
                int bookingID = Integer.parseInt(idRaw);
                BookingDAO dao = new BookingDAO();

                boolean isSuccess = dao.processCheckOut(bookingID, condition);

                if (isSuccess) {
                    response.sendRedirect("appointmentdetail?id=" + bookingID + "&status=success");

                } else {
                    response.sendRedirect("appointmentdetail?id=" + bookingID + "&status=error");
                }
            } else {
                response.sendRedirect("view-booking-list");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("view-booking-list?status=error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view-booking-list");
        }
    }
}