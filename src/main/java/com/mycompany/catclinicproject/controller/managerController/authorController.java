package com.mycompany.catclinicproject.controller.managerController;

import com.mycompany.catclinicproject.dao.homeDao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "authorController", urlPatterns = {"/author"})

public class authorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String idRaw = request.getParameter("userID");
        if (idRaw == null) {
            response.sendRedirect("account");
            return;
        }

        try {
            int userID = Integer.parseInt(idRaw);
            request.setAttribute("userID", userID);
            request.setAttribute("userName", userName);
            request.getRequestDispatcher("/WEB-INF/views/manager/authorAccount.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("listAccount");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDao dao = new UserDao();

        try {
            int userID = Integer.parseInt(request.getParameter("userID"));
            String roleName = request.getParameter("role");

            session.removeAttribute("error");
            session.removeAttribute("success");

            // ❌ chưa chọn role
            if (roleName == null) {
                session.setAttribute("error", "Please select a role.");
                response.sendRedirect("author?userID=" + userID);
                return;
            }

            int newRoleID = dao.getRoleID(roleName);
            int currentRoleID = dao.getRoleIdByUserId(userID);

            // ❌ role không đổi
            if (newRoleID == currentRoleID) {
                session.setAttribute("error", "User already has this role.");
                response.sendRedirect("author?userID=" + userID);
                return;
            }

            boolean updated = dao.updateUserRole(userID, newRoleID);

            if (updated) {
                session.setAttribute("success", "Role updated successfully.");
            } else {
                session.setAttribute("error", "Update failed.");
            }

            response.sendRedirect("author?userID=" + userID);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "System error!");
            response.sendRedirect("listAccount");
        }
    }
}
