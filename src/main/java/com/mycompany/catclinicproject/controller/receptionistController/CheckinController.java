package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.dao.CheckInBookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import java.io.IOException;
import java.time.LocalDate;

import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CheckinController", urlPatterns = {"/checkin"})
public class CheckinController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 3) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        BookingDAO dao = new BookingDAO();
        CheckInBookingDAO daoCheckin = new CheckInBookingDAO();
        int id = -1;

        try {
            String idRaw = request.getParameter("id");
            String status = request.getParameter("status");
            String condition = request.getParameter("condition");

            if (idRaw != null) {
                id = Integer.parseInt(idRaw);


                BookingHistoryDTO booking = dao.getBookingDetailByID(id);
                if (booking != null && "Completed".equals(status)) {
                    LocalDate appointmentDate = booking.getAppointmentDate().toLocalDate();
                    LocalDate today = LocalDate.now();


                    if (appointmentDate.isAfter(today)) {
                        response.sendRedirect("appointmentdetail?id=" + id + "&error=not_today");
                        return;
                    }
                }

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