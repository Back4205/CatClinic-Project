package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.FeedbackDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SubmitFeedbackController", urlPatterns = {"/submit-feedback"})
public class SubmitFeedbackController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            BookingDAO dao = new BookingDAO();
            var booking = dao.getBookingDetailByID(bookingID);

            if (booking == null || booking.getCheckOutTime() == null) {
                response.sendRedirect("booking-detail?id=" + bookingID);
                return;
            }

            FeedbackDTO fb = dao.getFeedbackByBookingID(bookingID);
            if (fb != null) {
                response.sendRedirect("booking-detail?id=" + bookingID);
                return;
            }

            dao.insertFeedback(bookingID, rating, comment);

            response.sendRedirect("booking-detail?id=" + bookingID);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("booking-history");
        }
    }
}