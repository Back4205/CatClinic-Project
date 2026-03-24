/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.Untils.CloudinaryUntil;
import com.mycompany.catclinicproject.dao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.VeterinarianDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "EditVeterinarianProfile", urlPatterns = {"/EditVeterinarianProfile"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class EditVeterinarianProfile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("acc");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        BookingDaoVeterinarian bdao = new BookingDaoVeterinarian();
        VeterinarianDTO vet = bdao.getVetProfile(user.getUserID());
        request.setAttribute("vet", vet);
        request.getRequestDispatcher("/WEB-INF/views/veterinarian/editVetProfile.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String degree = request.getParameter("degree");
        String bio = request.getParameter("bio");
        String expStr = request.getParameter("experienceYear");
        String genderStr = request.getParameter("male");

        String error = null;
        if (fullName == null || fullName.trim().isEmpty()) {
            error = "Họ tên không được để trống!";
        } else if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            error = "Email không hợp lệ!";
        } else if (phone == null || !phone.matches("\\d{10,11}")) {
            error = "Số điện thoại phải từ 10-11 số!";
        }

        int experienceYear = 0;
        try {
            experienceYear = Integer.parseInt(expStr);
            if (experienceYear < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            error = "Số năm kinh nghiệm phải là số dương!";
        }
        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
            return;
        }
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        VeterinarianDTO oldVet = dao.getVetProfile(user.getUserID());

        VeterinarianDTO v = new VeterinarianDTO();
        v.setUserID(user.getUserID());
        v.setVetID(oldVet.getVetID());
        v.setFullName(fullName);
        v.setMale(Boolean.valueOf(genderStr));
        v.setEmail(email);
        v.setPhone(phone);
        v.setDegree(degree);
        v.setExperienceYear(experienceYear);
        v.setBio(bio);

        // Xử lý ảnh
        Part filePart = request.getPart("image");
        String imageUrl = oldVet.getImage();
        if (filePart != null && filePart.getSize() > 0) {
            try {
                String fileName = "vet_" + user.getUserID();
                imageUrl = CloudinaryUntil.uploadImage(filePart, fileName, "my_vet");
            } catch (Exception e) {
                // Log lỗi upload nếu cần
            }
        }
        v.setImage(imageUrl);

        // Update DB
        dao.updateVetProfile(v);

        // 5. Cập nhật lại Session (Đảm bảo đồng bộ dữ liệu)
        user.setFullName(v.getFullName());
        user.setEmail(v.getEmail());
        user.setPhone(v.getPhone());
        user.setMale(v.getMale());
        session.setAttribute("acc", user); // Dùng lại key "acc"

        response.sendRedirect("VetProfile?success=1");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
