package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.ProfileDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "EditController", urlPatterns = {"/edit"})
public class EditController extends HttpServlet {

    /**
     * doGet: HIỂN THỊ FORM SỬA PROFILE
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // FIX CỨNG ID ĐỂ TEST
        int userID = 5;

        ProfileDAO dao = new ProfileDAO();
        User userProfile = dao.getUserProfile(userID);

        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp")
               .forward(request, response);
    }

    /**
     * doPost: XỬ LÝ UPDATE PROFILE
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int userID = 5;

        // 1. Lấy dữ liệu từ form
        String userName = request.getParameter("userName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        // 2. Set vào object User
        User u = new User();
        u.setUserID(userID);
        u.setUserName(userName);
        u.setPhone(phone);
        u.setEmail(email);
        u.setAddress(address);

        // 3. Update DB
        ProfileDAO dao = new ProfileDAO();
        boolean isUpdated = dao.updateProfile(u);

        if (isUpdated) {
            // Redirect về Profile để tránh submit lại form
            response.sendRedirect("accessprofile");
        } else {
            // Update fail → ở lại trang edit
            request.setAttribute("message", "Update failed. Please try again.");
            request.setAttribute("messageType", "error");
            request.setAttribute("user", u);

            request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp")
                   .forward(request, response);
        }
    }
}
