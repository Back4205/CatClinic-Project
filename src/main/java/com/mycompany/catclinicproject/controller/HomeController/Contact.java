package com.mycompany.catclinicproject.controller.HomeController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "Contact", urlPatterns = {"/Contact"})
public class Contact extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String messageText = request.getParameter("message");

        try {
            sendMail(fullName, email, phone, messageText);

            request.getSession().setAttribute("success",
                    "Gửi liên hệ thành công!");

        } catch (Exception e) {
            e.printStackTrace();

            request.getSession().setAttribute("error",
                    "Gửi thất bại! Vui lòng thử lại.");
        }

        // Redirect về HomeServlet + nhảy xuống contact
        response.sendRedirect(request.getContextPath() + "/loadinfo#contact");
    }

    private void sendMail(String fullName, String email,
                          String phone, String messageText) throws Exception {

        final String fromEmail = "glson0318@gmail.com";
        final String password = "ncon ehoq nplj ntwm"; // không có khoảng trắng

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(fromEmail));

        message.setSubject("glson0318@gmail.com");

        message.setText(
                "Họ tên: " + fullName +
                "\nEmail: " + email +
                "\nSĐT: " + phone +
                "\nNội dung: " + messageText
        );

        Transport.send(message);
    }
}