package com.mycompany.catclinicproject.controller.clientcontroller;

import com.mycompany.catclinicproject.dao.ProfileDAO;
import com.mycompany.catclinicproject.model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 👇 QUAN TRỌNG: Chỉ dùng đường dẫn "/edit" cho khớp với bạn
@WebServlet(name = "editController", urlPatterns = {"/edit"})
public class editController extends HttpServlet {

    /**
     * doGet: HIỂN THỊ FORM SỬA
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); 
        
        // --- FIX CỨNG ID = 5 ĐỂ TEST ---
        int userID = 5;
        // -------------------------------

        ProfileDAO dao = new ProfileDAO();
        User userProfile = dao.getUserProfile(userID);
        
        request.setAttribute("user", userProfile);
        request.getRequestDispatcher("/WEB-INF/views/client/EditProfile.jsp").forward(request, response);
    }

    /**
     * doPost: XỬ LÝ LƯU DỮ LIỆU
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // --- FIX CỨNG ID = 5 ---
        int userID = 5;

        // 1. Lấy dữ liệu
        String userName = request.getParameter("userName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        // 2. Tạo object User mới
        User u = new User();
        u.setUserID(userID);
        u.setUserName(userName);
        u.setPhone(phone);
        u.setEmail(email);
        u.setAddress(address);

        // 3. Gọi DAO update
        ProfileDAO dao = new ProfileDAO();
        boolean isUpdated = dao.updateProfile(u);

        if (isUpdated) {
            // --- THÀNH CÔNG ---
            request.setAttribute("message", "Update Profile Successfully!");
            request.setAttribute("messageType", "success");
            request.setAttribute("user", u); 
        } else {
            // --- THẤT BẠI ---
            request.setAttribute("message", "Update failed. Please try again.");
            request.setAttribute("messageType", "error");
            request.setAttribute("user", u);
        }

        // 4. Giữ nguyên trang để hiện thông báo
        request.getRequestDispatcher("/WEB-INF/views/client/EditProfile.jsp").forward(request, response);
    }
}