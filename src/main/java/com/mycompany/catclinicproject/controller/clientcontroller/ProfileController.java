package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.ProfileDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProfileController", urlPatterns = {"/accessprofile"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//         HttpSession session = request.getSession(false);
//     User user = (User)session.getAttribute("acc");
//     if(user == null){
//         response.sendRedirect(request.getContextPath()+"/login");
//         return;
//     }
//     int userID = user.getUserID();

         int userID = 5; 

        ProfileDAO dao = new ProfileDAO();
        User userProfile = dao.getUserProfile(userID);
        
        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/my-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userID = 5;
//         HttpSession session = request.getSession(false);
//     User user = (User)session.getAttribute("acc");
//     if(user == null){
//         response.sendRedirect(request.getContextPath()+"/login");
//         return;
//     }
//      int userID = user.getUserID();
        String action = request.getParameter("action");
        ProfileDAO dao = new ProfileDAO();

        // Condition: Check if the user action is "changePassword"
        if ("changePassword".equals(action)) {
            String oldPass = request.getParameter("oldPass");
            String newPass = request.getParameter("newPass");
            String confirmPass = request.getParameter("confirmPass");

            // Condition: Check if the new password and confirm password do NOT match
            if (!newPass.equals(confirmPass)) {
                request.setAttribute("message", "New passwords do not match!");
                request.setAttribute("messageType", "error");
            } 
            // Condition: Check if the old password is correct in the database
            else if (dao.checkPassword(userID, oldPass)) {
                dao.changePassword(userID, newPass);
                request.setAttribute("message", "Password updated successfully!");
                request.setAttribute("messageType", "success");
            } 
            // Condition: Execute this block if the old password is incorrect
            else {
                request.setAttribute("message", "Current password is incorrect!");
                request.setAttribute("messageType", "error");
            }
        }
        
        doGet(request, response);
    }
}