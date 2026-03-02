package com.mycompany.catclinicproject.controller.authentic;

import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.dao.homeDao.ServiceDao;
import com.mycompany.catclinicproject.model.Service;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.service.AuthService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "GoogleLoginController", urlPatterns = {"/login-google"})
public class GoogleLoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            // Lấy động đường dẫn URI hiện tại
            String redirectUri = request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath() + "/login-google";

            AuthService auth = new AuthService();
            // Truyền biến redirectUri vào hàm getToken
            String accessToken = auth.getToken(code, redirectUri);

            User googleUser = auth.getUserInfo(accessToken);

            if (googleUser != null) {
                UserDAO dao = new UserDAO();
                User userInDB = dao.checkLoginGoogle(googleUser.getEmail(), googleUser.getGoogleID());

                // Nếu chưa có tài khoản trong DB thì tạo mới
                if (userInDB == null) {
                    dao.createGoogleUser(googleUser);
                    userInDB = dao.checkLoginGoogle(googleUser.getEmail(), googleUser.getGoogleID());
                }

                // 👉 1. CHÈN THÊM BƯỚC CHECK TÀI KHOẢN BỊ KHÓA Ở ĐÂY
                if (!userInDB.isActive()) {
                    request.setAttribute("mess", "Your account has been deactivated by Admin! Please contact support.");
                    request.getRequestDispatcher("WEB-INF/views/auth/login.jsp").forward(request, response);
                    return; // Dừng luôn, không cho chạy xuống đoạn switch-case bên dưới
                }

                // Đăng nhập hợp lệ -> Lưu Session
                HttpSession session = request.getSession();
                session.setAttribute("acc", userInDB);
                int roleId = userInDB.getRoleID();

                // Luồng phân quyền của bạn
                switch (roleId) {
                    case 1:
                        request.getRequestDispatcher("WEB-INF/views/manager/AdminDashboard.jsp").forward(request, response);
                        break;
                    case 2:
                        response.sendRedirect("vet/schedule");
                        break;
                    case 3:
                        response.sendRedirect("reception/home");
                        break;
                    case 4:
                        String googleStaffPos = dao.getStaffPosition(userInDB.getUserID());

                        if ("Care".equalsIgnoreCase(googleStaffPos)) {
                            response.sendRedirect("care/tasks");
                            break;
                        }
                        if ("Technician".equalsIgnoreCase(googleStaffPos)) {
                            response.sendRedirect("technician/dashboard");
                            break;
                        }
                        break;
                    case 5:
                        ServiceDao sdao = new ServiceDao();
                        List<Service> serviceList = sdao.getAllService();
                        request.setAttribute("serviceList", serviceList);
                        request.getRequestDispatcher("WEB-INF/views/common/homeUser.jsp").forward(request, response);
                        break;
                    default:
                        response.sendRedirect("index.jsp");
                        break;
                }
            } else {

                request.setAttribute("mess", "Google Login Failed! Could not get user info.");
                request.getRequestDispatcher("WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } else {

            request.setAttribute("mess", "Google Login Cancelled!");
            request.getRequestDispatcher("WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}