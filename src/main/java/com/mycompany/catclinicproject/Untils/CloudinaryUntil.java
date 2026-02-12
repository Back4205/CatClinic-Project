/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.catclinicproject.Untils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;
import java.util.Map;

public class CloudinaryUntil {

    private static final Cloudinary cloudinary;

    static {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dydnbzspg",
                "api_key", "813244667935232",
                "api_secret", "OQ7OYuxF4yLZUKx99GYnymlsgzc",
                "secure", true
        ));
    }
    
    public static String uploadImage(Part part, String fileName) {
    try {
        byte[] fileBytes = part.getInputStream().readAllBytes();
        Map uploadResult = cloudinary.uploader().upload(
            fileBytes,
            ObjectUtils.asMap(
                "folder", "my_brand",
                "public_id", fileName,
                "resource_type", "image"
            )
        );

        return uploadResult.get("secure_url").toString();

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

}