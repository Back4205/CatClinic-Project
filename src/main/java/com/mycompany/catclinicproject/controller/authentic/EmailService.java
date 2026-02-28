package com.mycompany.catclinicproject.controller.authentic;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "thaodao2k5@gmail.com";
    private static final String APP_PASSWORD = "rgmqqvjbcxtnwbzw";

    public static boolean sendResetPasswordEmail(String toEmail, String resetLink) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM_EMAIL, "Cat Clinic Support"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject("Reset Your Cat Clinic Password", "UTF-8");

            String htmlContent = "<div style='font-family: Arial; padding: 20px; text-align: center; border: 1px solid #e5e7eb; border-radius: 10px;'>"
                    + "<h2 style='color: #f97316;'>Cat Clinic Password Reset</h2>"
                    + "<p>You have requested a password reset. Click the button below to set a new one:</p>"
                    + "<a href='" + resetLink + "' style='display: inline-block; padding: 10px 20px; background-color: #f97316; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>RESET PASSWORD</a>"
                    + "</div>";

            msg.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
