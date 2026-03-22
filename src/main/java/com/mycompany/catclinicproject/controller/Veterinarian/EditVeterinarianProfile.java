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
@WebServlet(name="EditVeterinarianProfile", urlPatterns={"/EditVeterinarianProfile"})
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
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        BookingDaoVeterinarian bdao = new BookingDaoVeterinarian();
        VeterinarianDTO vet = bdao.getVetProfile(user.getUserID());
        request.setAttribute("vet", vet);
        request.getRequestDispatcher("/WEB-INF/views/veterinarian/editVetProfile.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        VeterinarianDTO oldVet = dao.getVetProfile(user.getUserID());
        VeterinarianDTO v = new VeterinarianDTO();

        v.setUserID(user.getUserID());
        v.setVetID(oldVet.getVetID());

        v.setFullName(request.getParameter("fullName"));
        v.setMale(Boolean.valueOf(request.getParameter("male")));
        v.setEmail(request.getParameter("email"));
        v.setPhone(request.getParameter("phone"));
        v.setDegree(request.getParameter("degree"));
        v.setExperienceYear(Integer.valueOf(request.getParameter("experienceYear")));
        v.setBio(request.getParameter("bio"));

        // 🔥 upload image
        Part filePart = request.getPart("image");

        String imageUrl = oldVet.getImage();

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = "vet_" + user.getUserID();
            imageUrl = CloudinaryUntil.uploadImage(filePart, fileName, "my_vet");
        }

        v.setImage(imageUrl);

        dao.updateVetProfile(v);

        // update session
        user.setFullName(v.getFullName());
        user.setEmail(v.getEmail());
        user.setPhone(v.getPhone());
        user.setMale(v.getMale());

        session.setAttribute("user", user);

        response.sendRedirect("VetProfile?success=1");
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
