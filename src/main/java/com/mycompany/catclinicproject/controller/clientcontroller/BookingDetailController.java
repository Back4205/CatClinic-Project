package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.FeedbackDTO;
import java.io.IOException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BookingDetailController", urlPatterns = {"/booking-detail"})
public class BookingDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 5) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                BookingDAO dao = new BookingDAO();
                BookingHistoryDTO booking = dao.getBookingDetailByID(id);
                boolean k = dao.isNotCheckedOut(id);
                if (booking != null) {
                   if ("Completed".equalsIgnoreCase(booking.getStatus()) || "Done".equalsIgnoreCase(booking.getStatus())) {
                       FeedbackDTO feedback = dao.getFeedbackByBookingID(id);                       request.setAttribute("feedback", feedback); // Gửi biến này sang JSP
                    }
                    request.setAttribute("ischeckout",k);
                    request.setAttribute("booking", booking);
                    request.getRequestDispatcher("/WEB-INF/views/client/booking-detail.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("booking-history");
    }
}