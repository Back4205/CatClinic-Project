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
import java.util.regex.Pattern;

@WebServlet(name = "EditController", urlPatterns = {"/edit"})
public class EditController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        //      int userID = 5;
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int userID = user.getUserID();

        ProfileDAO dao = new ProfileDAO();
        UserDTO userProfile = dao.getUserProfile(userID);

        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đảm bảo không lỗi font tiếng Việt
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Lấy và làm sạch dữ liệu (giống RegisterController)
        String fullName = request.getParameter("fullName").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();

        String error = null;

        // 2. Sử dụng bộ Regex chuẩn mà cô đã khen
        // 2. Sử dụng bộ Regex chuẩn mà cô đã khen
        if (!Pattern.matches("^[\\p{L} .'-]+$", fullName)) {
            error = "Full Name must allow letters only and cannot contain numbers!";
        } else if (fullName.length() < 2) {
            error = "Full Name must be at least 2 characters!"; // Chặn tên 1 chữ
        } else if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email)) {
            error = "Invalid Email format! Email must include a domain (e.g., .com, .vn)";
        } else if (!Pattern.matches("^\\d{10}$", phone)) {
            error = "Phone number must be exactly 10 digits!";
        } else {
            // 3. Kiểm tra trùng lặp trong Database - CHỈ CHẠY KHI ĐỊNH DẠNG ĐÃ ĐÚNG
            ProfileDAO dao = new ProfileDAO();
            error = dao.checkDuplicate(email, phone, user.getUserID());
        }

        // 4. Xử lý khi có lỗi - ĐẢM BẢO DỪNG LẠI TẠI ĐÂY
        if (error != null) {
            request.setAttribute("message", error);
            request.setAttribute("messageType", "error");

            // Giữ lại dữ liệu người dùng vừa nhập để sửa
            UserDTO u = new UserDTO();
            u.setFullName(fullName); u.setPhone(phone); u.setEmail(email); u.setAddress(address);
            request.setAttribute("user", u);

            request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp").forward(request, response);
            return; // Dừng xử lý, tuyệt đối không cho xuống bước 5 cập nhật DB
        }

        // 5. Cập nhật Database - CHỈ CHẠY KHI error == null

        // 5. Cập nhật Database
        UserDTO updateDTO = new UserDTO();
        updateDTO.setUserID(user.getUserID());
        updateDTO.setFullName(fullName);
        updateDTO.setPhone(phone);
        updateDTO.setEmail(email);
        updateDTO.setAddress(address);

        ProfileDAO dao = new ProfileDAO();
        if (dao.updateProfile(updateDTO)) {
            // Đồng bộ Session ngay lập tức
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            session.setAttribute("acc", user);

            request.setAttribute("message", "Update Profile Successfully!");
            request.setAttribute("messageType", "success");
        } else {
            request.setAttribute("message", "System error during update. Please try again.");
            request.setAttribute("messageType", "error");
        }

        request.setAttribute("user", updateDTO);
        request.getRequestDispatcher("/WEB-INF/views/client/edit-profile.jsp").forward(request, response);
    }
}