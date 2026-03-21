package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import java.io.IOException;

import com.mycompany.catclinicproject.dao.CheckInBookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CheckinController", urlPatterns = {"/checkin"})
public class CheckinController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookingDAO dao = new BookingDAO();
        CheckInBookingDAO daoCheckin = new CheckInBookingDAO();
        int id = -1;

        try {
            String idRaw = request.getParameter("id");
            String status = request.getParameter("status");
            String condition = request.getParameter("condition");

            if (idRaw != null) {
                id = Integer.parseInt(idRaw);

                if ("Completed".equals(status)) {
                    boolean isSuccess = dao.checkInWithRecord(id, condition);

                    if (isSuccess) {
                        daoCheckin.checkInBooking(id);
                        response.sendRedirect("appointmentdetail?id=" + id + "&status=checkin_success");
                        return;
                    }
                } else {
                    dao.updateBookingStatus(id, status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("view-booking-list");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controller xử lý Check-in và chuyển hướng về trang chi tiết";
    }
}