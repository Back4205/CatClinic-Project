package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet (name = "CatDeleteController", urlPatterns = {"/cats/cat-delete"})
public class CatDeleteController extends HttpServlet {
    protected  void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("catId"));
        CatDAO dao = new CatDAO();
        dao.deleteCat(id);
        response.sendRedirect(request.getContextPath() + "/cats");
    }

}
