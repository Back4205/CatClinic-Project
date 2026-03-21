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

        response.sendRedirect(request.getContextPath() + "/loadinfo#contact");
    }

    private void sendMail(String fullName, String email,
            String phone, String messageText) throws Exception {

        final String fromEmail = "glson0318@gmail.com";
        final String password = "htsz kmcg nmao jdvf"; 

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
                new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(fromEmail, password);

            }
        });

        session.setDebug(true);

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(fromEmail));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("glson0318@gmail.com")
        );

        message.setReplyTo(new Address[]{
            new InternetAddress(email)
        });

        message.setSubject("New Contact From Cat Clinic Website");

        message.setText(
                "Bạn vừa nhận được một liên hệ mới từ website Cat Clinic.\n\n"
                + "Họ tên: " + fullName
                + "\nEmail: " + email
                + "\nSĐT: " + phone
                + "\n\nNội dung:\n" + messageText
        );

        Transport.send(message);
    }
}