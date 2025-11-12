package com.ys.hr.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.common.core.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrBackgroundCheckInfoMapper;
import com.ys.hr.domain.HrBackgroundCheckInfo;
import com.ys.hr.service.IHrBackgroundCheckInfoService;
import com.ys.common.core.utils.DateUtils;

/**
 * Background check personnel information Service Implementation
 *
 * @author ys
 * @date 2025-06-25
 */
@Service
public class HrBackgroundCheckInfoServiceImpl extends ServiceImpl<HrBackgroundCheckInfoMapper, HrBackgroundCheckInfo> implements IHrBackgroundCheckInfoService
{

    @Value("${backgroundcheck.clientId}")
    private String clientId;

    @Value("${backgroundcheck.clientSecret}")
    private String clientSecret;

    /**
     * Query Background check personnel information
     *
     * @param id Background check personnel information primary key
     * @return Background check personnel information
     */
    @Override
    public HrBackgroundCheckInfo selectHrBackgroundCheckInfoById(Long id)
    {
        HrBackgroundCheckInfo hrBackgroundCheckInfo = baseMapper.selectHrBackgroundCheckInfoById(id);
        if (StringUtils.isNotEmpty(hrBackgroundCheckInfo.getEducationList())){
            String educationList = hrBackgroundCheckInfo.getEducationList();
            hrBackgroundCheckInfo.setEducations(JSON.parseArray(educationList));
        }
        if (StringUtils.isNotEmpty(hrBackgroundCheckInfo.getEmploymentList())){
            String employmentList = hrBackgroundCheckInfo.getEmploymentList();
            hrBackgroundCheckInfo.setEmployments(JSON.parseArray(employmentList));
        }
        return hrBackgroundCheckInfo;
    }

    /**
     * Query Background check personnel information list
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Background check personnel information
     */
    @Override
    public List<HrBackgroundCheckInfo> selectHrBackgroundCheckInfoList(HrBackgroundCheckInfo hrBackgroundCheckInfo)
    {
        return baseMapper.selectHrBackgroundCheckInfoList(hrBackgroundCheckInfo);
    }

    /**
     * Add Background check personnel information
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Result
     */
    @Override
    public int insertHrBackgroundCheckInfo(HrBackgroundCheckInfo hrBackgroundCheckInfo)
    {
        if (ObjectUtils.isNotEmpty(hrBackgroundCheckInfo.getBirthdate())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            hrBackgroundCheckInfo.setDateOfBirth(sdf.format(hrBackgroundCheckInfo.getBirthdate()));
        }
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(hrBackgroundCheckInfo);
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
            if (response.code() != 200){
                throw new RuntimeException(jsonObject.toJSONString());
            }
            hrBackgroundCheckInfo.setCandidateId(jsonObject.getString("id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        hrBackgroundCheckInfo.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrBackgroundCheckInfo);
    }

    /**
     * Update Background check personnel information
     *
     * @param hrBackgroundCheckInfo Background check personnel information
     * @return Result
     */
    @Override
    public int updateHrBackgroundCheckInfo(HrBackgroundCheckInfo hrBackgroundCheckInfo)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("id", hrBackgroundCheckInfo.getCandidateId());
        params.put("firstName", hrBackgroundCheckInfo.getFirstName());
        params.put("lastName", hrBackgroundCheckInfo.getLastName());
        params.put("middleName", hrBackgroundCheckInfo.getMiddleName());
        params.put("suffix", hrBackgroundCheckInfo.getSuffix());
        params.put("ssn", hrBackgroundCheckInfo.getSsn());
        params.put("email", hrBackgroundCheckInfo.getEmail());
        params.put("phone", hrBackgroundCheckInfo.getPhone());
        params.put("address", hrBackgroundCheckInfo.getAddress());
        params.put("city", hrBackgroundCheckInfo.getCity());
        params.put("region", hrBackgroundCheckInfo.getRegion());
        params.put("country", hrBackgroundCheckInfo.getCountry());
        params.put("postalCode", hrBackgroundCheckInfo.getPostalCode());
        params.put("educations", hrBackgroundCheckInfo.getEducations());
        params.put("employments", hrBackgroundCheckInfo.getEmployments());
        params.put("prevEmployed", true);
        params.put("convicted", false);
        if (ObjectUtils.isNotEmpty(hrBackgroundCheckInfo.getBirthdate())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            params.put("dateOfBirth", sdf.format(hrBackgroundCheckInfo.getBirthdate()));
        }
        // Filter out key-value pairs where the value is null.
        Map<String, Object> filtered = params.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(filtered);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json
        );
        Request request = new Request.Builder()
                .url("https://api.accuratebackground.com/v3/candidate/" + hrBackgroundCheckInfo.getCandidateId())
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
        hrBackgroundCheckInfo.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrBackgroundCheckInfo);
    }

    /**
     * Batch delete Background check personnel information
     *
     * @param ids Background check personnel information primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrBackgroundCheckInfoByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Background check personnel information information
     *
     * @param id Background check personnel information primary key
     * @return Result
     */
    @Override
    public int deleteHrBackgroundCheckInfoById(String id)
    {
        return baseMapper.deleteById(id);
    }

    private String createToken(){
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }

}
