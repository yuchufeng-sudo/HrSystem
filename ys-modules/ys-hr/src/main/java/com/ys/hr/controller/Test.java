package com.ys.hr.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

public class Test {
    public static void main(String[] args) {
        String clientId = "1784d97c-8fe7-4f4e-8cec-ae092f2f1559";
        String clientSecret = "809c79c4-40b5-4d60-8213-1a7970031b62";
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpResponse response = HttpRequest.post("https://api.accuratebackground.com/v3/oauth/token")
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("grant_type=client_credentials")
                .timeout(5000)
                .execute();

        String accessToken = response.body();
        System.out.println("Access Token: " + accessToken);

    }

}
