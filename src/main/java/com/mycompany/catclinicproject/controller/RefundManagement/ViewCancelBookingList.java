/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.RefundManagement;

import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.CancelBookingDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="ViewCancelBookingList", urlPatterns={"/ViewCancelBookingList"})
public class ViewCancelBookingList extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BookingDAO dao = new BookingDAO();
        List<CancelBookingDTO> list = dao.getAllCancelBookings();

        request.setAttribute("cancelList", list);

        request.getRequestDispatcher("/WEB-INF/views/manager/cancelBookings.jsp")
               .forward(request, response);
    }

}
