package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CatAddcontroller", urlPatterns = {"/cats/cat-add"})
public class CatAddcontroller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cat", null);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String message = "";
        try {

            int ownerID = Integer.parseInt(request.getParameter("ownerID"));
            String name = request.getParameter("name");
            String breed = request.getParameter("breed");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int age = Integer.parseInt(request.getParameter("age"));

            if (name == null || name.trim().isEmpty()) {
                message = "Name is required!";
            } else if (age < 0) {
                message = "Age must be >= 0!";
            }
            if (!message.isEmpty()) {
                Cat cat = new Cat();
                cat.setOwnerID(ownerID);
                cat.setName(name);
                cat.setBreed(breed);
                cat.setGender(gender);
                cat.setAge(age);
                request.setAttribute("cat", cat);
                request.setAttribute("message", message);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }
            Cat cat = new Cat();
            cat.setOwnerID(ownerID);
            cat.setName(name);
            cat.setBreed(breed);
            cat.setGender(gender);
            cat.setAge(age);
            CatDAO catDAO = new CatDAO();
            catDAO.addCat(cat);
            response.sendRedirect(request.getContextPath() + "/cats");
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid input data!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                    .forward(request, response);
        }
    }
}
