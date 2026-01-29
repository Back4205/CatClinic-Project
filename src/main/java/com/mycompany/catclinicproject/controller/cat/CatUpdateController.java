package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet (name = "CatUpdateController", urlPatterns = {"/cats/cat-update"})
public class CatUpdateController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("catId"));
        CatDAO catDAO = new CatDAO();
        Cat  cat = catDAO.getCatByID(id)   ;
        request.setAttribute("cat", cat);

        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("catId"));
        String name = request.getParameter("name");
        int newAge = Integer.parseInt(request.getParameter("age"));

        CatDAO catDAO = new CatDAO();
        Cat cat = catDAO.getCatByID(id);

        int oldAge = cat.getAge();

        if (newAge < oldAge || newAge > oldAge + 1) {
            request.setAttribute("message", "Age must be the same or increase by 1 year only.");
            request.setAttribute("cat", cat); // QUAN TRỌNG
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
            return;
        }


        cat.setName(name);
        cat.setAge(newAge);
        catDAO.updateCat(cat);


        response.sendRedirect(request.getContextPath() + "/cats");
    }


}
