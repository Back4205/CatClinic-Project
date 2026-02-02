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

@WebServlet(name = "CatUpdateController", urlPatterns = {"/cats/cat-update"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class CatUpdateController extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/FU-learning/SPRING-2026_ky5/SWP391/CatClinicimg/cats";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int catId = Integer.parseInt(request.getParameter("catId"));

        CatDAO dao = new CatDAO();
        Cat cat = dao.getCatByID(catId);

        request.setAttribute("cat", cat);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        try {

            int catId = Integer.parseInt(request.getParameter("catId"));
            String name = request.getParameter("name");
            int newAge = Integer.parseInt(request.getParameter("age"));

            CatDAO dao = new CatDAO();
            HttpSession session = request.getSession(false);
            User user = (User)session.getAttribute("acc");
            CatDAO catDAO = new CatDAO();
            if(user == null){
                response.sendRedirect(request.getContextPath()+"/login");
                return;
            }
            int userID = user.getUserID();
            int ownerID = catDAO.getOwnerIdByUserId(userID);

            Cat cat = dao.getCatByID(catId);
            if (cat == null || cat.getOwnerID() != ownerID) {
                session.setAttribute("message", "You are not allowed to update this cat!");
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }
            int oldAge = cat.getAge();
            String message = "";


            if (dao.checkCatNameExistUpdate(cat.getOwnerID(), name, catId)) {
                message = "This owner already has a cat with this name!";
            }
            else if (newAge < oldAge) {
                    message = "Age cannot be decreased!";
            } else if (newAge > oldAge + 1) {
                    message = "Age can only increase by 1 year!";
            }



            if (!message.isEmpty()) {
                request.setAttribute("message", message);
                request.setAttribute("cat", cat);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }



            String imagePath = cat.getImg();
            Part filePart = request.getPart("image");// lay file tu request browse

            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String savedName = System.currentTimeMillis() + "_" + fileName;
                filePart.write(uploadDir.getAbsolutePath() + File.separator + savedName);
                imagePath = "image/cats/" + savedName;
            }

            cat.setName(name);
            cat.setAge(newAge);
            cat.setImg(imagePath);

            dao.updateCat(cat);

            response.sendRedirect(request.getContextPath() + "/cats");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Update cat failed!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
        }
    }
}
