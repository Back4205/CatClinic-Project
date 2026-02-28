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

@WebServlet(name = "CatAddController", urlPatterns = {"/cats/cat-add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class CatAddController extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/FU-learning/SPRING-2026_ky5/SWP391/CatClinicimg/cats";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nhận tham số 'from' để biết quay lại trang nào sau khi hoàn tất
        String from = request.getParameter("from");
        request.setAttribute("from", from);

        request.setAttribute("cat", null);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CatDAO catDAO = new CatDAO();
        String message = "";

        // Luôn lấy 'from' ở đầu doPost để sử dụng trong cả trường hợp thành công và thất bại
        String from = request.getParameter("from");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int ownerID = catDAO.getOwnerIdByUserId(user.getUserID());

        try {
            String name = request.getParameter("name");
            String breed = request.getParameter("breed");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int age = Integer.parseInt(request.getParameter("age"));

            // Kiểm tra trùng tên mèo của cùng một chủ
            if (catDAO.checkCatNameExistbyOwnerID(ownerID, name)) {
                message = "This owner already has a cat with this name!";
            }

            if (!message.isEmpty()) {
                Cat cat = new Cat();
                cat.setOwnerID(ownerID);
                cat.setName(name);
                cat.setBreed(breed);
                cat.setGender(gender);
                cat.setAge(age);

                // Cần set lại 'from' để thẻ hidden trong JSP không bị mất giá trị khi forward
                request.setAttribute("from", from);
                request.setAttribute("cat", cat);
                request.setAttribute("message", message);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }

            // Xử lý Upload Ảnh
            Part filePart = request.getPart("image");
            String imagePath = "image/cats/default.jpg";

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

            Cat cat = new Cat();
            cat.setOwnerID(ownerID);
            cat.setName(name);
            cat.setBreed(breed);
            cat.setGender(gender);
            cat.setAge(age);
            cat.setIsActive(1);
            cat.setImg(imagePath);

            catDAO.addCat(cat);

            // Điều hướng dựa trên nguồn gốc của yêu cầu
            if ("booking".equals(from)) {
                response.sendRedirect(request.getContextPath() + "/Booking");
            } else {
                response.sendRedirect(request.getContextPath() + "/cats");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Đảm bảo 'from' vẫn tồn tại nếu quay lại form do lỗi hệ thống
            request.setAttribute("from", from);
            request.setAttribute("message", "Add cat failed!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
        }
    }
}