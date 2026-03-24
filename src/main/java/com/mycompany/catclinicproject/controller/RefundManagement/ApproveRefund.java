/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.RefundManagement;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.BookingDAO;
import com.mycompany.catclinicproject.model.CancelBookingDTO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="ApproveRefund", urlPatterns={"/ApproveRefund"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class ApproveRefund extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int bookingID = Integer.parseInt(request.getParameter("bookingID"));
        BookingDAO bdao = new BookingDAO();
        CancelBookingDTO cBooking = bdao.getPendingCancelBookingById(bookingID);
        request.setAttribute("bookingID", bookingID);
        request.setAttribute("cancelbooking", cBooking);
        request.getRequestDispatcher("/WEB-INF/views/manager/approveRefund.jsp")
                .forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int bookingID = Integer.parseInt(request.getParameter("bookingID"));
        Part filePart = request.getPart("refundImage");
        String fileName = "refund_" + System.currentTimeMillis();
        String imageUrl = CloudinaryUntil.uploadImage(filePart, fileName,"my_refund");
        BookingDAO bdao = new BookingDAO();
        bdao.approveRefund(bookingID, imageUrl);
        response.sendRedirect(request.getContextPath() + "/ViewCancelBookingList");
    }
}
