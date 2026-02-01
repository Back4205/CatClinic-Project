package com.mycompany.catclinicproject.controller;

import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.model.RegisterDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final String NAME_PATTERN = "^[\\p{L} .'-]+$";

    private static final String PHONE_PATTERN = "^\\d{10}$";

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{5,20}$";

    private static final String PASS_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullname").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPassword");

        RegisterDTO dto = new RegisterDTO(fullName, email, phone, address, username, password, confirmPass);

        String error = null;

        if (!Pattern.matches(NAME_PATTERN, fullName)) {
            error = "Full Name must allow letters only and cannot contain numbers!";
        } else if (!Pattern.matches(EMAIL_PATTERN, email)) {
            error = "Invalid Email format!";
        } else if (!Pattern.matches(PHONE_PATTERN, phone)) {
            error = "Phone number must be exactly 10 digits!";
        } else if (!Pattern.matches(USERNAME_PATTERN, username)) {
            error = "Username must be 5-20 characters, no spaces or special chars!";
        } else if (!Pattern.matches(PASS_PATTERN, password)) {
            error = "Password must be at least 6 characters and contain both letters and numbers!";
        } else if (!password.equals(confirmPass)) {
            error = "Confirm Password does not match!";
        } else {
            UserDAO dao = new UserDAO();

            if (dao.checkUserExists(username, email)) {
                error = "Username or Email already exists in the system!";
            } else if (!password.equals(confirmPass)) {
                error = "Confirm Password does not match!";

            } else {

                boolean isSuccess = dao.registerCustomer(dto);
                if (isSuccess) {
                    request.setAttribute("mess", "Registration successful! Please login.");
                    request.getRequestDispatcher("WEB-INF/views/auth/login.jsp").forward(request, response);
                    return;
                } else {
                    error = "System error during registration. Please try again.";
                }
            }
        }

        request.setAttribute("error", error);
        request.setAttribute("data", dto);
        request.getRequestDispatcher("WEB-INF/views/auth/register.jsp").forward(request, response);
    }

}
