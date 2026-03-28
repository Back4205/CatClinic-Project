package com.mycompany.catclinicproject.controller.receptionistController;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.BookingHistoryDTO;
import com.mycompany.catclinicproject.model.BoardingRecordDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AppointmentDetailController", urlPatterns = {"/appointmentdetail"})
public class AppointmentDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idRaw = request.getParameter("id");
            System.out.println(">>> GO TO DETAIL");
            System.out.println("ID RAW: " + idRaw);
            if (idRaw == null) {
                response.sendRedirect("view-booking-list");
                return;
            }
            
            int bookingID = Integer.parseInt(idRaw);

            BookingDAO dao = new BookingDAO();

            BookingHistoryDTO detail = dao.getBookingDetailByID2(bookingID);
            request.setAttribute("booking", detail);

            BoardingRecordDTO boarding = dao.getBoardingRecordByBookingID(bookingID);
            request.setAttribute("boarding", boarding);

            request.getRequestDispatcher("/WEB-INF/views/reception/appointment-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
//            response.sendRedirect("view-booking-list");
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Controller xử lý hiển thị chi tiết lịch hẹn và hồ sơ nội trú";
    }
}