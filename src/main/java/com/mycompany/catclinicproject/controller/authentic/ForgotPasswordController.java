package com.mycompany.catclinicproject.controller.authentic;

import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.controller.authentic.EmailService;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/forgot-password"})
public class ForgotPasswordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/auth/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        if(email != null) email = email.trim();

        UserDAO dao = new UserDAO();

        if (dao.checkEmailExists(email)) {
            // 1. Sinh Token bằng UUID
            String token = UUID.randomUUID().toString();

            // 2. LƯU TOKEN VÀO SESSION (Để không phải sửa DB)
            HttpSession session = request.getSession();
            session.setAttribute("resetToken", token);
            session.setAttribute("resetEmail", email);
            session.setMaxInactiveInterval(900); // Tồn tại 15 phút

            // 3. Tạo link gửi qua mail
            String resetLink = request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath()
                    + "/reset-password?token=" + token;

            boolean isSent = EmailService.sendResetPasswordEmail(email, resetLink);

            if (isSent) {
                request.setAttribute("successMess", "A reset link has been sent! Please check your email.");
            } else {
                request.setAttribute("errorMess", "Failed to send email. Please check server config.");
            }
        } else {
            request.setAttribute("errorMess", "The email address does not exist in the system!");
        }

        request.setAttribute("email", email);
        request.getRequestDispatcher("WEB-INF/views/auth/forgot-password.jsp").forward(request, response);
    }
}