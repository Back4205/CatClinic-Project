package com.mycompany.catclinicproject.controller;

import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.service.AuthService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "GoogleLoginController", urlPatterns = {"/login-google"})
public class GoogleLoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            AuthService auth = new AuthService();

            String accessToken = auth.getToken(code);

            User googleUser = auth.getUserInfo(accessToken);

            if (googleUser != null) {
                UserDAO dao = new UserDAO();

                User userInDB = dao.checkLoginGoogle(googleUser.getEmail(), googleUser.getGoogleID());

                if (userInDB == null) {

                    dao.createGoogleUser(googleUser);

                    userInDB = dao.checkLoginGoogle(googleUser.getEmail(), googleUser.getGoogleID());
                }

                HttpSession session = request.getSession();
                session.setAttribute("acc", userInDB);
                response.sendRedirect("home");
            } else {
                response.sendRedirect("login?mess=Google Login Failed");
            }
        } else {

            response.sendRedirect("login?mess=Login Cancelled");
        }
    }
}
