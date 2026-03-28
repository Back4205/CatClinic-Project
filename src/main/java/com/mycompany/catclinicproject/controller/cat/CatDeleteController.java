package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet (name = "CatDeleteController", urlPatterns = {"/cats/cat-delete"})
public class CatDeleteController extends HttpServlet {
    protected  void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 5) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        int id = Integer.parseInt(request.getParameter("catId"));
        CatDAO dao = new CatDAO();
        dao.deleteOrDeactivateCat(id);
        response.sendRedirect(request.getContextPath() + "/cats");
    }

}