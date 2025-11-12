package com.ys.hr.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.common.core.aspect.RateLimit;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/backgroundCheck")
public class HrBackgroundCheck extends BaseController {

    @Value("${backgroundcheck.clientId}")
    private String clientId;

    @Value("${backgroundcheck.clientSecret}")
    private String clientSecret;

    /**
     *
     * @return
     */
    @PostMapping("/createCandidate")
    public AjaxResult createCandidate(@RequestBody Map<String, Object> params) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/candidate")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", createToken())
                .build();
        String responseBody = null;
        try {
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();
            JSONObject jsonObject = JSON.parseObject(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success();
    }

    /**
     *
     * @return
     */
    @GetMapping("/getCandidate/{id}")
    public AjaxResult getCandidate(@PathVariable("id") String id){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/candidate/" + id)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("authorization", createToken())
                .build();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
            if (response.code() != 200) {
                throw new RuntimeException(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(JSON.parse(responseBody));
    }

    /**
     *
     * @return
     */
    @PutMapping("/updateCandidate")
    @RateLimit(limit = 2, prefix = "background:update_candidate")
    public AjaxResult updateCandidate(@RequestBody Map<String, Object> params){
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/candidate/" + params.get("id"))
                .put(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", createToken())
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new RuntimeException(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success();
    }

    /**
     *
     * @return
     */
    @PostMapping("/createOrder")
    @RateLimit(limit = 5, prefix = "background_check:create_order")
    public AjaxResult createOrder(@RequestBody Map<String, Object> params){
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/order")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", createToken())
                .build();
        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            System.out.println(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success();
    }

    /**
     *
     * @return
     */
    @GetMapping("/getOrder/{id}")
    public AjaxResult getOrder(@PathVariable String id){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/order/" + id)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("authorization", createToken())
                .build();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(JSON.parse(responseBody));
    }

    /**
     *
     * @return
     */
    @GetMapping("/getReport/{reportId}")
    public AjaxResult getReport (@PathVariable("reportId") String reportId){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/report/" + reportId)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("authorization", createToken())
                .build();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            responseBody = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(JSON.parse(responseBody));
    }

    private String createToken(){
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
}
