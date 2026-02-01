package com.mycompany.catclinicproject.controller;

import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("cUser")) {
                    request.setAttribute("username", c.getValue());
                }
                if (c.getName().equals("cPass")) {
                    request.setAttribute("password", c.getValue());
                    request.setAttribute("remember", "checked");
                }
            }
        }

        request.getRequestDispatcher("WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String u = request.getParameter("username");
        String p = request.getParameter("password");
        String r = request.getParameter("remember");

        UserDAO dao = new UserDAO();
        User account = dao.checkLogin(u, p);

        if (account == null) {

            request.setAttribute("mess", "Invalid Username/Email or Password!");

            request.setAttribute("username", u);
            request.getRequestDispatcher("WEB-INF/views/auth/login.jsp").forward(request, response);
        } else {

            HttpSession session = request.getSession();
            session.setAttribute("acc", account);

            Cookie cu = new Cookie("cUser", u);
            Cookie cp = new Cookie("cPass", p);

            if (r != null) {
                cu.setMaxAge(60 * 60 * 24 * 7);
                cp.setMaxAge(60 * 60 * 24 * 7);
            } else {
                cu.setMaxAge(0);
                cp.setMaxAge(0);
            }
            response.addCookie(cu);
            response.addCookie(cp);

            switch (account.getRoleID()) {
                case 1:
                    response.sendRedirect("manager/dashboard");
                    break;
                case 2:
                    response.sendRedirect("vet/schedule");
                    break;
                case 3:
                    response.sendRedirect("reception/home");
                    break;
                case 4:
                    response.sendRedirect("staff/tasks");
                    break;
                case 5:
                    response.sendRedirect("home");
                    break;
                default:
                    response.sendRedirect("home");
            }
        }
    }
}
