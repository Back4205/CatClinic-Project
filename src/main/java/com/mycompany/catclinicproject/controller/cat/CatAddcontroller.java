package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "CatAddcontroller", urlPatterns = {"/cats/cat-add"})
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5 // 5MB
)
public class CatAddcontroller extends HttpServlet {

    private static final String UPLOAD_DIR = "img/cats";

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

            //  UPLOAD IMAGE
            Part filePart = request.getPart("image");
            String imagePath = null;

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName())
                        .getFileName().toString();

                String realPath = getServletContext().getRealPath("/");
                File uploadDir = new File(realPath + UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String savedName = System.currentTimeMillis() + "_" + fileName;
                filePart.write(uploadDir.getAbsolutePath() + File.separator + savedName);


                imagePath = UPLOAD_DIR + "/" + savedName;
            }


            if (!message.isEmpty()) {
                Cat cat = new Cat();
                cat.setOwnerID(ownerID);
                cat.setName(name);
                cat.setBreed(breed);
                cat.setGender(gender);
                cat.setAge(age);
                cat.setIsActive(1);
                cat.setImg(imagePath);

                request.setAttribute("cat", cat);
                request.setAttribute("message", message);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                        .forward(request, response);
                return;
            }

            Cat cat = new Cat();
            cat.setOwnerID(ownerID);
            cat.setName(name);
            cat.setBreed(breed);
            cat.setGender(gender);
            cat.setAge(age);
            cat.setIsActive(1);
            cat.setImg(imagePath);

            new CatDAO().addCat(cat);

            response.sendRedirect(request.getContextPath() + "/cats");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Upload error!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                    .forward(request, response);
        }
    }
}
