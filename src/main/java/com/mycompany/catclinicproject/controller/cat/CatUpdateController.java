package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.model.Cat;
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
        try {
            int catId = Integer.parseInt(request.getParameter("catId"));
            CatDAO dao = new CatDAO();
            Cat cat = dao.getCatByID(catId);
            request.setAttribute("cat", cat);
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/cats");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CatDAO dao = new CatDAO();
        String message = "";

        try {
            int catId = Integer.parseInt(request.getParameter("catId"));
            String name = request.getParameter("name");
            String breed = request.getParameter("breed");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int newAge = Integer.parseInt(request.getParameter("age"));

            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("acc");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int ownerID = dao.getOwnerIdByUserId(user.getUserID());
            Cat cat = dao.getCatByID(catId);

            if (cat == null || cat.getOwnerID() != ownerID) {
                session.setAttribute("message", "You are not allowed to update this cat!");
                response.sendRedirect(request.getContextPath() + "/cats");
                return;
            }

            // 1. Kiểm tra validation văn bản
            String letterOnlyRegex = "^[a-zA-ZÀ-ỹ\\s]+$";
            if (dao.checkCatNameExistUpdate(ownerID, name, catId)) {
                message = "This owner already has a cat with this name!";
            } else if (newAge < cat.getAge()) {
                message = "Age cannot be decreased!";
            } else if (newAge > cat.getAge() + 1) {
                message = "Age can only increase by 1 year!";
            } else if (breed == null || breed.trim().isEmpty()) {
                message = "Breed cannot be empty!";
            } else if (!breed.matches(letterOnlyRegex)) {
                message = "Breed cannot contain numbers or special characters!";
            }

            String imagePath = cat.getImg();
            Part filePart = request.getPart("image");

            // 2. Xử lý Ảnh
            if (message.isEmpty() && filePart != null && filePart.getSize() > 0) {
                // Kiểm tra định dạng qua MIME type và nội dung thực tế
                if (!isImageFile(filePart)) {
                    message = "The uploaded file is not a valid image (JPG, PNG, GIF, or JFIF only)!";
                } else {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    File uploadDir = new File(UPLOAD_DIR);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String savedName = System.currentTimeMillis() + "_" + fileName;
                    // Ghi file
                    filePart.write(uploadDir.getAbsolutePath() + File.separator + savedName);
                    imagePath = "image/cats/" + savedName;
                }
            }

            // 3. Phản hồi nếu có lỗi
            if (!message.isEmpty()) {
                request.setAttribute("message", message);
                request.setAttribute("cat", cat);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }

            // 4. Cập nhật Model
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


    public boolean isImageFile(Part filePart) {
        String contentType = filePart.getContentType();
        // Kiểm tra MIME type cơ bản
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }
        try (java.io.InputStream is = filePart.getInputStream()) {
            // ImageIO.read sẽ giải mã cấu trúc file để xác nhận nó có phải ảnh thật không
            java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(is);
            return bi != null;
        } catch (IOException e) {
            return false;
        }
    }
}