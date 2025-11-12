package com.ys.hr.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrBackgroundCheckOrder;
import com.ys.hr.mapper.HrBackgroundCheckOrderMapper;
import com.ys.hr.service.IHrBackgroundCheckOrderService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Background Check Order Service Implementation
 *
 * @author ys
 * @date 2025-06-25
 */
@Service
public class HrBackgroundCheckOrderServiceImpl extends ServiceImpl<HrBackgroundCheckOrderMapper, HrBackgroundCheckOrder> implements IHrBackgroundCheckOrderService
{

    @Value("${backgroundcheck.clientId}")
    private String clientId;

    @Value("${backgroundcheck.clientSecret}")
    private String clientSecret;

    /**
     * Query Background Check Order
     *
     * @param id Background Check Order primary key
     * @return Background Check Order
     */
    @Override
    public HrBackgroundCheckOrder selectHrBackgroundCheckOrderById(Long id)
    {
        return baseMapper.selectHrBackgroundCheckOrderById(id);
    }

    /**
     * Query Background Check Order list
     *
     * @param hrBackgroundCheckOrder Background Check Order
     * @return Background Check Order
     */
    @Override
    public List<HrBackgroundCheckOrder> selectHrBackgroundCheckOrderList(HrBackgroundCheckOrder hrBackgroundCheckOrder)
    {
        return baseMapper.selectHrBackgroundCheckOrderList(hrBackgroundCheckOrder);
    }

    /**
     * Add Background Check Order
     *
     * @param hrBackgroundCheckOrder Background Check Order
     * @return Result
     */
    @Override
    public int insertHrBackgroundCheckOrder(HrBackgroundCheckOrder hrBackgroundCheckOrder)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("packageType", hrBackgroundCheckOrder.getPackageType());
        map.put("workflow", "EXPRESS");
        map.put("candidateId", hrBackgroundCheckOrder.getCandidateId());
        map.put("jobLocation", hrBackgroundCheckOrder.getJobLocation());
        if (hrBackgroundCheckOrder.getComplianceTokens().size() > 0){
            map.put("complianceTokens", hrBackgroundCheckOrder.getComplianceTokens());
        }
        map.put("copyOfReport", false);
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(map);
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
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (response.code() != 200){
                throw new RuntimeException(jsonObject.toJSONString());
            }
            hrBackgroundCheckOrder.setStatus(jsonObject.getString("status"));
            hrBackgroundCheckOrder.setOrderId(jsonObject.getString("id"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Create order exception");
        }
        hrBackgroundCheckOrder.setJobLocationList(hrBackgroundCheckOrder.getJobLocation().toJSONString());
        hrBackgroundCheckOrder.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBackgroundCheckOrder);
    }

    /**
     * Update Background Check Order
     *
     * @param hrBackgroundCheckOrder Background Check Order
     * @return Result
     */
    @Override
    public int updateHrBackgroundCheckOrder(HrBackgroundCheckOrder hrBackgroundCheckOrder)
    {
        hrBackgroundCheckOrder.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBackgroundCheckOrder);
    }

    /**
     * Batch delete Background Check Order
     *
     * @param ids Background Check Order primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBackgroundCheckOrderByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Background Check Order information
     *
     * @param id Background Check Order primary key
     * @return Result
     */
    @Override
    public int deleteHrBackgroundCheckOrderById(String id)
    {
        return baseMapper.deleteById(id);
    }

    @Override
    public int refreshStatus(HrBackgroundCheckOrder hrBackgroundCheckOrder) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/order/" + hrBackgroundCheckOrder.getOrderId())
                .get()
                .addHeader("accept", "application/json")
                .addHeader("authorization", createToken())
                .build();
        Response response = null;
        String responseBody = null;
        try {
            response = client.newCall(request).execute();
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            if (response.code() != 200){
                throw new RuntimeException(jsonObject.toJSONString());
            }
            hrBackgroundCheckOrder.setStatus(jsonObject.getString("status"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hrBackgroundCheckOrder.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBackgroundCheckOrder);
    }

    private String createToken(){
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
}
