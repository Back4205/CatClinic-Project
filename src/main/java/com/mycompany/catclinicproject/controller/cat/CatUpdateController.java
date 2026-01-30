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

@WebServlet(name = "CatUpdateController", urlPatterns = {"/cats/cat-update"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB
public class CatUpdateController extends HttpServlet {

    private static final String UPLOAD_DIR = "img/cats";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int catId = Integer.parseInt(request.getParameter("catId"));

        CatDAO dao = new CatDAO();
        Cat cat = dao.getCatByID(catId);

        request.setAttribute("cat", cat);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int catId = Integer.parseInt(request.getParameter("catId"));
        String name = request.getParameter("name");
        int newAge = Integer.parseInt(request.getParameter("age"));

        CatDAO dao = new CatDAO();
        Cat cat = dao.getCatByID(catId);

        int oldAge = cat.getAge();

        if (newAge < oldAge || newAge > oldAge + 1) {
            request.setAttribute("message", "Age must be the same or increase by 1 year only.");
            request.setAttribute("cat", cat);
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                    .forward(request, response);
            return;
        }

        Part filePart = request.getPart("image");

        // mặc định giữ ảnh cũ
        String imagePath = cat.getImg();

        if (filePart != null && filePart.getSize() > 0) {

            String fileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();

            String realPath = getServletContext().getRealPath("/");
            File uploadDir = new File(realPath + File.separator + UPLOAD_DIR);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String savedName = System.currentTimeMillis() + "_" + fileName;
            filePart.write(uploadDir.getAbsolutePath()
                    + File.separator + savedName);

            // DB chỉ lưu đường dẫn
            imagePath = UPLOAD_DIR + "/" + savedName;
        }
        cat.setName(name);
        cat.setAge(newAge);
        cat.setImg(imagePath);

        dao.updateCat(cat);

        response.sendRedirect(request.getContextPath() + "/cats");
    }
}
