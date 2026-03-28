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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null || user.getRoleID() != 5) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

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
            String breed = request.getParameter("breed");
            int gender = Integer.parseInt(request.getParameter("gender"));

            int newAge = Integer.parseInt(request.getParameter("age"));




            CatDAO dao = new CatDAO();
            HttpSession session = request.getSession(false);
            User user = (User)session.getAttribute("acc");
            CatDAO catDAO = new CatDAO();
            if(user == null || user.getRoleID() != 5) {
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
            String imagePath = cat.getImg();
            Part filePart = request.getPart("image");// lay file tu folder trong laptop
            String message = "";

            String letterOnlyRegex = "^[a-zA-ZÀ-ỹ\\s]+$";

            if (dao.checkCatNameExistUpdate(cat.getOwnerID(), name, catId)) {
                message = "This owner already has a cat with this name!";
            }
            else if (newAge < oldAge) {
                message = "Age cannot be decreased!";
            } else if (newAge > oldAge + 1) {
                message = "Age can only increase by 1 year!";
            }else if (breed == null || breed.trim().isEmpty()) {
                message = "breed cannot be empty!";
            } else if (!breed.matches(letterOnlyRegex)) {
                message = "breed cannot contain numbers or special characters!";
            }
            else if (filePart != null && filePart.getSize() > 0) {
                if (filePart.getSize() > 1 * 1024 * 1024) {
                    message = "Image size must be less than 1MB!";
                }

                else if (!isImageFile(filePart)) {
                    message = "The uploaded file is not a valid image (it might be a renamed text/exe file)!";
                }
            }


            Cat tempCat = new Cat();
            tempCat.setCatID(catId);
            tempCat.setOwnerID(ownerID);
            tempCat.setName(name);
            tempCat.setBreed(breed);
            tempCat.setGender(gender);
            tempCat.setAge(newAge);
            tempCat.setImg(cat.getImg());
            if (!message.isEmpty()) {
                request.setAttribute("message", message);
                request.setAttribute("cat", tempCat);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }



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
            cat.setBreed(breed);
            cat.setGender(gender);

            dao.updateCat(cat);

            response.sendRedirect(request.getContextPath() + "/cats");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Update cat failed!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
        }
    }
    public  boolean isImageFile(Part filePart) {
        //Trình duyệt khi gửi file lên sẽ đính kèm một thông tin gọi là MIME Type
        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }
        // mở luồng dữ liệu để đọc trực tiếp các byte bên trong file.
        // nó sẽ cố gắng đọc cấu trúc dữ liệu bên trong file để dựng thành một đối tượng
        try (java.io.InputStream is = filePart.getInputStream()) {
            java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(is);
            return bi != null; // Trả về true nếu thực sự là ảnh
        } catch (IOException e) {
            return false;
        }
    }
}