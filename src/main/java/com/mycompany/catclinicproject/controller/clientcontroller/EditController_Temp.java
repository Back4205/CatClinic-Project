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
public class EditController_Temp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); 
        
        int userID = 5;

        ProfileDAO dao = new ProfileDAO();
        User userProfile = dao.getUserProfile(userID);
        
        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        int userID = 5;

        String userName = request.getParameter("userName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        User u = new User();
        u.setUserID(userID);
        u.setUserName(userName);
        u.setPhone(phone);
        u.setEmail(email);
        u.setAddress(address);

        ProfileDAO dao = new ProfileDAO();
        boolean isUpdated = dao.updateProfile(u);

        // Condition: Check if the update operation was successful (returns true)
        if (isUpdated) {
            request.setAttribute("message", "Update Profile Successfully!");
            request.setAttribute("messageType", "success");
            request.setAttribute("user", u); 
        } 
        // Condition: Execute this block if the update operation failed (returns false)
        else {
            request.setAttribute("message", "Update failed. Please try again.");
            request.setAttribute("messageType", "error");
            request.setAttribute("user", u);
        }

        request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp").forward(request, response);
    }
}