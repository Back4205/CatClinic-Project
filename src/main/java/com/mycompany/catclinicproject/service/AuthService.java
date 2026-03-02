package com.mycompany.catclinicproject.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.catclinicproject.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthService {

    public static final String GOOGLE_CLIENT_ID = "766431550241-pg69p1ohgg79ori80f0bf2n5e3i0cfc1.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "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";

    public static final String GOOGLE_REDIRECT_URI = "http://localhost:9999/CatClinicProject/login-google";

    public static final String GOOGLE_GRANT_TYPE = "authorization_code";
    public static final String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    public String getToken(String code) throws IOException {
        URL url = new URL(GOOGLE_LINK_GET_TOKEN);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String params = "code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(GOOGLE_CLIENT_ID, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(GOOGLE_CLIENT_SECRET, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(GOOGLE_REDIRECT_URI, StandardCharsets.UTF_8)
                + "&grant_type=" + URLEncoder.encode(GOOGLE_GRANT_TYPE, StandardCharsets.UTF_8);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        JsonObject jobj = new Gson().fromJson(response.toString(), JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public User getUserInfo(String accessToken) throws IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        JsonObject googleUser = new Gson().fromJson(response.toString(), JsonObject.class);

        User user = new User();

        if (googleUser.has("id")) {
            user.setGoogleID(googleUser.get("id").getAsString());
        }
        if (googleUser.has("email")) {
            user.setEmail(googleUser.get("email").getAsString());
        }
        if (googleUser.has("name")) {
            user.setFullName(googleUser.get("name").getAsString());
        }

        if (user.getEmail() != null) {
            user.setUserName(user.getEmail());
        }

        return user;
    }
    private static AuthService instance;
}
