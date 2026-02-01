package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.ProfileDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 👇 ĐÃ SỬA: Đường dẫn là /accessprofile
@WebServlet(name = "ProfileController", urlPatterns = {"/accessprofile"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- FIX CỨNG ID = 5 ĐỂ TEST ---
        int userID = 5; 

        ProfileDAO dao = new ProfileDAO();
        User userProfile = dao.getUserProfile(userID);
        
        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/MyProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // --- FIX CỨNG ID = 5 ---
        int userID = 5;

        String action = request.getParameter("action");
        ProfileDAO dao = new ProfileDAO();

        // Logic đổi mật khẩu
        if ("changePassword".equals(action)) {
            String oldPass = request.getParameter("oldPass");
            String newPass = request.getParameter("newPass");
            String confirmPass = request.getParameter("confirmPass");

            if (!newPass.equals(confirmPass)) {
                request.setAttribute("message", "New passwords do not match!");
                request.setAttribute("messageType", "error");
            } else if (dao.checkPassword(userID, oldPass)) {
                dao.changePassword(userID, newPass);
                request.setAttribute("message", "Password updated successfully!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Current password is incorrect!");
                request.setAttribute("messageType", "error");
            }
        }
        
        // Load lại trang
        doGet(request, response);
    }
}