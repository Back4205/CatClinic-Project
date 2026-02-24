package com.mycompany.catclinicproject.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPayConfig {

    public static final String vnp_TmnCode = "3XA4EGXP";
    public static final String vnp_HashSecret = "S7JYOZNY652POLRSZJFUSW8VFF0LSNWP";
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    public static final String vnp_ReturnUrl =
            "http://localhost:9999/CatClinicProject/vnpay-return";

    // HASH ALL FIELDS
    public static String hashAllFields(Map<String, String> fields) {

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {

            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);

            if (fieldValue != null && fieldValue.length() > 0) {

                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    hashData.append("&");
                }
            }
        }

        return hmacSHA512(vnp_HashSecret, hashData.toString());
    }

    //  HMAC SHA512
    public static String hmacSHA512(String key, String data) {

        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey =
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");

            hmac.init(secretKey);

            byte[] hashBytes =
                    hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }
}