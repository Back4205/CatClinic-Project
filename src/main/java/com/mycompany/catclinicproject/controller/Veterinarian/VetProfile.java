/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.catclinicproject.controller.Veterinarian;

import com.mycompany.catclinicproject.dao.BookingDaoVeterinarian;
import com.mycompany.catclinicproject.dao.ProfileDAO;
import com.mycompany.catclinicproject.model.User;
import com.mycompany.catclinicproject.model.VeterinarianDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="VetProfile", urlPatterns={"/VetProfile"})
public class VetProfile extends HttpServlet {
        private static final String PASS_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
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
        BookingDaoVeterinarian dao = new BookingDaoVeterinarian();
        VeterinarianDTO vet = dao.getVetProfile(user.getUserID());
        request.setAttribute("vet", vet);
        request.getRequestDispatcher("/WEB-INF/views/veterinarian/vetProfile.jsp").forward(request, response);
    }
    
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    HttpSession session = request.getSession(false);
    User user = (User) session.getAttribute("acc");

    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    int userID = user.getUserID();
    String action = request.getParameter("action");

    ProfileDAO dao = new ProfileDAO();

    if ("changePassword".equals(action)) {

        String oldPass = request.getParameter("oldPass");
        String newPass = request.getParameter("newPass");
        String confirmPass = request.getParameter("confirmPass");

        if (oldPass == null || newPass == null || confirmPass == null
                || oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {

            request.setAttribute("message", "Please fill in all password fields!");
            request.setAttribute("messageType", "error");

        } else if (!dao.checkPassword(userID, oldPass)) {

            request.setAttribute("message", "Current password is incorrect!");
            request.setAttribute("messageType", "error");

        } else if (!newPass.matches(PASS_PATTERN)) {

            request.setAttribute("message",
                    "Password must be at least 6 characters, including letters and numbers!");
            request.setAttribute("messageType", "error");

        } else if (!newPass.equals(confirmPass)) {

            request.setAttribute("message", "New passwords do not match!");
            request.setAttribute("messageType", "error");

        } else {

            dao.changePassword(userID, newPass);

            request.setAttribute("message", "Password updated successfully!");
            request.setAttribute("messageType", "success");
        }
    }

    BookingDaoVeterinarian daoVet = new BookingDaoVeterinarian();
    VeterinarianDTO vet = daoVet.getVetProfile(userID);

    request.setAttribute("vet", vet);

    request.getRequestDispatcher("/WEB-INF/views/veterinarian/vetProfile.jsp")
            .forward(request, response);
}

}
