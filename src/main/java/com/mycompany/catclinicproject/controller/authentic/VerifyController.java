package com.mycompany.catclinicproject.controller.authentic;

import com.mycompany.catclinicproject.dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="VerifyController", urlPatterns={"/verify"})
public class VerifyController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy token từ URL (người dùng click từ email)
        String token = request.getParameter("token");

        // 2. Lấy Token và Email lưu tạm trong Session từ lúc Đăng ký
        HttpSession session = request.getSession();
        String sessionToken = (String) session.getAttribute("registerToken");
        String sessionEmail = (String) session.getAttribute("registerEmail");

        try {
            // Kiểm tra Token trên URL có khớp với Token trong Session không
            if (token != null && sessionToken != null && token.equals(sessionToken)) {

                UserDAO dao = new UserDAO();
                // Chạy hàm kích hoạt tài khoản
                boolean isVerified = dao.activateUser(sessionEmail);

                if (isVerified) {
                    // Xác thực thành công -> Hủy token
                    session.removeAttribute("registerToken");
                    session.removeAttribute("registerEmail");

                    // Chuyển hướng về login kèm thông báo thành công
                    response.sendRedirect(request.getContextPath() + "/login?mess=Account verified successfully! Please log in.");
                } else {
                    response.sendRedirect(request.getContextPath() + "/login?mess=System error: Could not activate account.");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/login?mess=The verification link is invalid or has expired!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login?mess=System Error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}