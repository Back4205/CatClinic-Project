package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.ProfileDAO;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.UserDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    private static final String PASS_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         HttpSession session = request.getSession(false);
     User user = (User)session.getAttribute("acc");
     if(user == null){
         response.sendRedirect(request.getContextPath()+"/login");
         return;
     }
     int userID = user.getUserID();

//        int userID = 5;

        ProfileDAO dao = new ProfileDAO();
        UserDTO userProfile = dao.getUserProfile(userID);

        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/my-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

//        int userID = 5;
         HttpSession session = request.getSession(false);
     User user = (User)session.getAttribute("acc");
     if(user == null){
         response.sendRedirect(request.getContextPath()+"/login");
         return;
     }
      int userID = user.getUserID();
        String action = request.getParameter("action");
        ProfileDAO dao = new ProfileDAO();

        if ("changePassword".equals(action)) {

            String oldPass = request.getParameter("oldPass");
            String newPass = request.getParameter("newPass");
            String confirmPass = request.getParameter("confirmPass");

            if (oldPass == null || newPass == null || confirmPass == null
                    || oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {

                request.setAttribute("message", "Please fill in all password fields!");
                request.setAttribute("messageType", "error");

            } else if (!dao.checkPassword(userID, oldPass)) {

                request.setAttribute("message", "Current password is incorrect!");
                request.setAttribute("messageType", "error");

            } else if (!newPass.matches(PASS_PATTERN)) {

                request.setAttribute("message",
                        "Password must be at least 6 characters, including letters and numbers!");
                request.setAttribute("messageType", "error");

            } else if (!newPass.equals(confirmPass)) {

                request.setAttribute("message", "New passwords do not match!");
                request.setAttribute("messageType", "error");

            } else {

                dao.changePassword(userID, newPass);
                request.setAttribute("message", "Password updated successfully!");
                request.setAttribute("messageType", "success");

            }
        }

        request.getRequestDispatcher("/WEB-INF/views/client/my-profile.jsp")
                .forward(request, response);
    }

}
