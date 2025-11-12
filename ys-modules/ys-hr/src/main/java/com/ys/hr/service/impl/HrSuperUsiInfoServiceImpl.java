package com.ys.hr.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrSuperUsiInfoMapper;
import com.ys.hr.domain.HrSuperUsiInfo;
import com.ys.hr.service.IHrSuperUsiInfoService;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.X509TrustManager;

/**
 * USI and product information table Service Implementation
 *
 * @author ys
 * @date 2025-10-23
 */
@Service
public class HrSuperUsiInfoServiceImpl extends ServiceImpl<HrSuperUsiInfoMapper, HrSuperUsiInfo> implements IHrSuperUsiInfoService
{

    @Override
    public List<HrSuperUsiInfo> selectHrSuperUsiInfoList(HrSuperUsiInfo hrSuperUsiInfo)
    {
        return baseMapper.selectHrSuperUsiInfoList(hrSuperUsiInfo);
    }


    private static final String USI_DOWNLOAD_URL = "https://superfundlookup.gov.au/Tools/DownloadUsiList?download=usi";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<HrSuperUsiInfo> downloadAndParseUSIFile() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(USI_DOWNLOAD_URL).addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36").build();
        Response response = okHttpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to download USI file, response code：" + response.code());
        }
        if (response.body() == null) {
            throw new IOException("The content of the USI file is empty");
        }

        List<HrSuperUsiInfo> usiInfoList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() != 481) {
                    continue;
                }
                HrSuperUsiInfo dto = parseSingleLine(line);
                if (dto != null) {
                    usiInfoList.add(dto);
                }
            }
        }
        return usiInfoList;
    }

    private HrSuperUsiInfo parseSingleLine(String line) {
        try {
            HrSuperUsiInfo dto = new HrSuperUsiInfo();
            dto.setAbn(substring(line, 0, 11));
            dto.setFundName(substring(line, 12, 212));
            dto.setUsi(substring(line, 213, 233));
            dto.setProductName(substring(line, 234, 434));
            dto.setContributionRestriction(substring(line, 435, 435));
            String fromDateStr = substring(line, 460, 470).trim();
            dto.setFromDate(LocalDate.parse(fromDateStr, DATE_FORMATTER));
            String toDateStr = substring(line, 471, 481).trim();
            dto.setToDate(StringUtils.isEmpty(toDateStr) ? null : LocalDate.parse(toDateStr, DATE_FORMATTER));
            dto.setCreateTime(DateUtils.getNowDate());
            if (StringUtils.isEmpty(dto.getUsi()) || StringUtils.isEmpty(dto.getAbn())) {
                return null;
            }
            return dto;
        } catch (Exception e) {
            return null;
        }
    }

    private String substring(String line, int startIdx, int endIdx) {
        if (startIdx < 0 || endIdx >= line.length()) {
            return "";
        }
        return line.substring(startIdx, endIdx + 1).trim();
    }

    @Transactional
    @Override
    public void batchSaveOrUpdate(List<HrSuperUsiInfo> entityList) {
        if (entityList.isEmpty()) {
            return;
        }
        for (HrSuperUsiInfo entity : entityList) {
            QueryWrapper<HrSuperUsiInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("usi", entity.getUsi())
                    .eq("from_date", entity.getFromDate());
            HrSuperUsiInfo existingEntity = baseMapper.selectOne(queryWrapper);
            if (existingEntity != null) {
                entity.setId(existingEntity.getId());
                baseMapper.updateById(entity);
            } else {
                baseMapper.insert(entity);
            }
        }
    }
}
