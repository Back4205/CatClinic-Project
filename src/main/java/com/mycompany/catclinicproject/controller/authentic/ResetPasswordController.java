package com.mycompany.catclinicproject.controller.authentic;

import com.mycompany.catclinicproject.dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ResetPasswordController", urlPatterns = {"/reset-password"})
public class ResetPasswordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("resetToken");

        if (token == null || sessionToken == null || !token.equals(sessionToken)) {
            request.setAttribute("errorMess", "The reset link is invalid or has expired!");
        } else {
            request.setAttribute("token", token);
        }
        request.getRequestDispatcher("WEB-INF/views/auth/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("resetToken");
        String sessionEmail = (String) session.getAttribute("resetEmail");

        // 1. Kiểm tra Token
        if (token == null || sessionToken == null || !token.equals(sessionToken)) {
            request.setAttribute("errorMess", "The reset link is invalid or has expired!");
            request.getRequestDispatcher("WEB-INF/views/auth/reset-password.jsp").forward(request, response);
            return;
        }

        String errorMsg = null;

        // 2. Logic Kiểm tra Mật khẩu của bạn
        if (newPass == null || newPass.isEmpty() || confirmPass == null || confirmPass.isEmpty()) {
            errorMsg = "Please enter all password fields!";
        } else if (!newPass.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
            errorMsg = "Password must be at least 6 characters and contain both letters and numbers!";
        } else if (!newPass.equals(confirmPass)) {
            errorMsg = "Confirm password does not match!";
        }

        if (errorMsg != null) {
            request.setAttribute("errorMess", errorMsg);
            request.setAttribute("token", token);
            request.getRequestDispatcher("WEB-INF/views/auth/reset-password.jsp").forward(request, response);
            return;
        }

        // 3. Cập nhật vào Database
        try {
            UserDAO dao = new UserDAO();
            if (dao.updatePasswordByEmail(sessionEmail, newPass)) {
                // Hủy Session sau khi dùng xong
                session.removeAttribute("resetToken");
                session.removeAttribute("resetEmail");

                response.sendRedirect(request.getContextPath() + "/login?mess=Password reset successfully! Please log in.");
            } else {
                request.setAttribute("errorMess", "System error: Could not update password.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("WEB-INF/views/auth/reset-password.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorMess", "System error: " + e.getMessage());
            request.setAttribute("token", token);
            request.getRequestDispatcher("WEB-INF/views/auth/reset-password.jsp").forward(request, response);
        }
    }
}