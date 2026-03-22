package com.mycompany.catclinicproject.util;

import java.security.MessageDigest;
import java.util.Base64;

public class PasswordUtil {
    // Hàm mã hóa mật khẩu sử dụng thuật toán SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        // Thay tenHamMaHoaCuaBan bằng đúng tên hàm bạn viết nhé
        System.out.println(hashPassword("123"));
    }
}
