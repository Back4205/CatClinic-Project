package com.mycompany.catclinicproject.controller.cat;

import com.mycompany.catclinicproject.dao.CatDAO;
import com.mycompany.catclinicproject.dao.UserDAO;
import com.mycompany.catclinicproject.model.Cat;
import com.mycompany.catclinicproject.model.Owner;
import com.mycompany.catclinicproject.model.RegisterDTO;
import com.mycompany.catclinicproject.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "CatAddController", urlPatterns = {"/cats/cat-add"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 6, maxRequestSize = 1024 * 1024 * 10)
public class CatAddController extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/FU-learning/SPRING-2026_ky5/SWP391/CatClinicimg/cats";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String from = request.getParameter("from");
        String from2 = request.getParameter("f");
        String phone = request.getParameter("phone");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");

        if ("Counterbooking".equals(from2)) {
            String errorMsg = "";

            if (phone == null || !phone.matches("^0\\d{9,10}$")) {
                errorMsg = "Phone number must start with 0 and contain 10-11 digits!";
            } else if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("^[\\p{L}\\s]+$")) {
                errorMsg = "Full name can only contain letters (no special characters)!";
            } else if (email == null || email.trim().isEmpty() || !email.matches("^[\\w.-]+@gmail\\.com$")) {
                errorMsg = "Email must be a valid @gmail.com address!";
            }


            if (!errorMsg.isEmpty()) {
                request.getSession().setAttribute("error", errorMsg);
                String redirectUrl = request.getContextPath() + "/Booking2?phone=" + phone + "&fullName=" + fullName + "&email=" + email;
                response.sendRedirect(redirectUrl);
                return;
            }
        }


        request.setAttribute("from", from);
        request.setAttribute("f", from2);
        request.setAttribute("phone", phone);
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);

        request.setAttribute("cat", null);
        request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CatDAO catDAO = new CatDAO();
        UserDAO userDAO = new UserDAO();
        String message = "";
        String letterOnlyRegex = "^[a-zA-ZÀ-ỹ\\s]+$";

        String from = request.getParameter("from");
        String from2 = request.getParameter("from2");
        String phone = request.getParameter("phone");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("acc") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int ownerID = 0;


        if ("Counterbooking".equals(from2) && phone != null && !phone.trim().isEmpty()) {


            String errorMsg = "";


            if (!phone.matches("^0\\d{9,10}$")) {
                errorMsg = "Phone number must start with 0 and contain 10-11 digits!";
            }

            else if (fullName == null || fullName.trim().isEmpty() || !fullName.matches("^[\\p{L}\\s]+$")) {
                errorMsg = "Full name can only contain letters (no numbers or special characters)!";
            }

            else if (email == null || email.trim().isEmpty() || !email.matches("^[\\w.-]+@gmail\\.com$")) {
                errorMsg = "Email must be a valid @gmail.com address!";
            }


            if (!errorMsg.isEmpty()) {
                request.setAttribute("from", from);
                request.setAttribute("f", from2);
                request.setAttribute("phone", phone);
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);


                Cat tempCat = new Cat();
                tempCat.setName(request.getParameter("name"));
                tempCat.setBreed(request.getParameter("breed"));
                try {
                    tempCat.setGender(Integer.parseInt(request.getParameter("gender")));
                    tempCat.setAge(Integer.parseInt(request.getParameter("age")));
                } catch (NumberFormatException ignored) {}
                request.setAttribute("cat", tempCat);

                request.setAttribute("message", errorMsg);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }



            Owner existingCustomer = userDAO.getUserByPhone(phone);

            if (existingCustomer == null) {
                RegisterDTO newCustomer = new RegisterDTO();
                newCustomer.setFullName(fullName);
                newCustomer.setPhone(phone);
                newCustomer.setEmail(email);
                newCustomer.setPassword(phone);

                userDAO.registerCustomer(newCustomer);


                Owner justCreatedCustomer = userDAO.getUserByPhone(phone);
                if (justCreatedCustomer != null) {
                    ownerID = catDAO.getOwnerIdByUserId(justCreatedCustomer.getUserID());
                }

            } else {

                ownerID = catDAO.getOwnerIdByUserId(existingCustomer.getUserID());
            }
        } else {

            ownerID = catDAO.getOwnerIdByUserId(user.getUserID());
        }

        try {
            String name = request.getParameter("name");
            String breed = request.getParameter("breed");
            String  genderStr = request.getParameter("gender");
            String ageStr = request.getParameter("age");

            if( name == null || name.trim().isEmpty()){
                message = "Cat name cannot be empty!";
            }
            else if (catDAO.checkCatNameExistbyOwnerID(ownerID, name)) {
                message = "This owner already has a cat with this name!";
            }
            else if (breed == null || breed.trim().isEmpty()) {
                message = "breed cannot be empty!";
            } else if (!breed.matches(letterOnlyRegex)) {
                message = "breed cannot contain numbers or special characters!";
            }
            else if (genderStr == null || ageStr == null || ageStr.trim().isEmpty()) {
                message = "Gender and Age are required!";
            }

            int gender = Integer.parseInt(genderStr);
            int age = Integer.parseInt(ageStr);
            if (age < 0 || age >15 ){
                message = "Age must be between 0 and 15";
            }
            if (!message.isEmpty()) {

                Cat cat = new Cat();
                cat.setOwnerID(ownerID);
                cat.setName(name);
                cat.setBreed(breed);
                cat.setGender(gender);
                cat.setAge(age);


                request.setAttribute("from", from);
                request.setAttribute("f", from2);
                request.setAttribute("cat", cat);
                request.setAttribute("message", message);
                request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                return;
            }

            // Xử lý Upload Ảnh
            Part filePart = request.getPart("image");
            String imagePath = "image/cats/default.jpg";

            if (filePart != null && filePart.getSize() > 0) {
                if (filePart.getSize() > 5 * 1024 * 1024) {

                    request.setAttribute("from", from);
                    request.setAttribute("from2", from2);
                    request.setAttribute("phone", phone);
                    request.setAttribute("fullName", fullName);
                    request.setAttribute("email", email);

                    // giữ lại dữ liệu cat
                    Cat cat = new Cat();
                    cat.setOwnerID(ownerID);
                    cat.setName(name);
                    cat.setBreed(breed);
                    cat.setGender(gender);
                    cat.setAge(age);

                    request.setAttribute("cat", cat);
                    request.setAttribute("message", "Image size must be less than 5MB!");
                    request.setAttribute("from", from);
                    request.setAttribute("from2", from2);
                    request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp")
                            .forward(request, response);
                    return;
                }

                if (!isImageFile(filePart)) {
                    request.setAttribute("from", from);
                    request.setAttribute("message", "Invalid image! Please upload a real image file.");
                    Cat cat = new Cat();
                    cat.setOwnerID(ownerID);
                    cat.setName(name);
                    cat.setBreed(breed);
                    cat.setGender(gender);
                    cat.setAge(age);

                    request.setAttribute("cat", cat);
                    request.setAttribute("from", from);
                    request.setAttribute("from2", from2);
                    request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
                    return;
                }
                // filePart.getSubmittedFileName() lấy tên file mà t đặt trong máy
                // loại bỏ các đường dẫn dạ . chỉ lấy tên file và đuôi file
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
            cat.setCatID(0);
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
            }else if("Counterbooking".equals(from2)){
                String redirectUrl = request.getContextPath() + "/Booking2";
                // Nếu có số điện thoại, gắn nó vào URL để trang Booking tự load lại thông tin
                if (phone != null && !phone.trim().isEmpty()) {
                    redirectUrl += "?phone=" + phone;
                }
                response.sendRedirect(redirectUrl);
            }else {
                response.sendRedirect(request.getContextPath() + "/cats");
            }

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("from", from);
            request.setAttribute("from2", from2);
            request.setAttribute("message", "Add cat failed!");
            request.getRequestDispatcher("/WEB-INF/views/client/cat-form.jsp").forward(request, response);
        }
    }


    public  boolean isImageFile(Part filePart) {
        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }
        try (java.io.InputStream is = filePart.getInputStream()) {
            java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(is);
            return bi != null; // Trả về true nếu thực sự là ảnh
        } catch (IOException e) {
            return false;
        }
    }
}