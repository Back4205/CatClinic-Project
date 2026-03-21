package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "NotificationController", urlPatterns = {"/reception/clear-notification"})
public class NotificationController  extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        BookingDAO dao = new BookingDAO();

        int latestID = dao.getLatestBookingID();

        // mark as seen
        session.setAttribute("lastSeenBookingID", latestID);

        // clear notification
        session.setAttribute("notificationCount", 0);
        session.setAttribute("notifications", new ArrayList<>());

        String referer = request.getHeader("referer");
        response.sendRedirect(referer);
    }

}