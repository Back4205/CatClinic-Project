package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
import com.mycompany.catclinicproject.model.Owner;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "CatAddController", urlPatterns = {"/cats/cat-add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)

public class CatAddController extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/FU-learning/SPRING-2026_ky5/SWP391/CatClinicimg/cats";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("cat", null);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CatDAO catDAO = new CatDAO();
        String message = "";
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("acc");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        int userID = user.getUserID();
        int ownerID = catDAO.getOwnerIdByUserId(userID);
        try {

          //  int ownerID = Integer.parseInt(request.getParameter("ownerID"));  // test
            String name = request.getParameter("name");
            String breed = request.getParameter("breed");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int age = Integer.parseInt(request.getParameter("age"));


          if (catDAO.checkCatNameExistbyOwnerID(ownerID, name )){
              message = "This owner already has a cat with this name!";
          }


            if (!message.isEmpty()) {
                Cat cat = new Cat();
                cat.setOwnerID(ownerID);
                cat.setName(name);
                cat.setBreed(breed);
                cat.setGender(gender);
                cat.setAge(age);
                cat.setIsActive(1);

                request.setAttribute("cat", cat);
                request.setAttribute("message", message);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }


            Part filePart = request.getPart("image"); // lay file tu request browse
            String imagePath = "image/cats/default.jpg";

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                File uploadDir = new File(UPLOAD_DIR); // dai dien cho thu muc
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String savedName = System.currentTimeMillis() + "_" + fileName;
                filePart.write(uploadDir.getAbsolutePath() + File.separator + savedName);
                imagePath = "image/cats/" + savedName;
            }

            Cat cat = new Cat();
            cat.setOwnerID(ownerID);
            cat.setName(name);
            cat.setBreed(breed);
            cat.setGender(gender);
            cat.setAge(age);
            cat.setIsActive(1);
            cat.setImg(imagePath);

            catDAO.addCat(cat);

            response.sendRedirect(request.getContextPath() + "/cats");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Add cat failed!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
        }
    }
}
