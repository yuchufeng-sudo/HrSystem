package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ExternalServiceException;
import com.ys.hr.domain.HrSuperUsiInfo;
import com.ys.hr.mapper.HrSuperUsiInfoMapper;
import com.ys.hr.service.IHrSuperUsiInfoService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * USI Information Service Implementation (Optimized Version)
 *
 * @author ys
 * @date 2025-10-23
 */
@Slf4j
@Service
public class HrSuperUsiInfoServiceImpl extends ServiceImpl<HrSuperUsiInfoMapper, HrSuperUsiInfo> implements IHrSuperUsiInfoService {

    @Value("${usi.download.url}")
    private String usiDownloadUrl;

    @Value("${usi.download.timeout}")
    private int downloadTimeout;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_RETRIES = 3;

    @Override
    public List<HrSuperUsiInfo> selectHrSuperUsiInfoList(HrSuperUsiInfo hrSuperUsiInfo) {
        return baseMapper.selectHrSuperUsiInfoList(hrSuperUsiInfo);
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

    /**
     * Download and parse USI file (with retry mechanism)
     */
    @Override
    @Retryable(
            value = ExternalServiceException.class,
            maxAttempts = MAX_RETRIES,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Transactional(rollbackFor = Exception.class)
    public List<HrSuperUsiInfo> downloadAndParseUSIFile() {

        log.info("Starting USI file download from: {}", usiDownloadUrl);

        OkHttpClient client = null;
        Response response = null;
        BufferedReader reader = null;

        try {
            // Create HTTP client
            client = createHttpClient();

            // Create request
            Request request = new Request.Builder()
                    .url(usiDownloadUrl)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build();

            // Execute request
            response = client.newCall(request).execute();

            // Check response status
            if (!response.isSuccessful()) {
                String errorMsg = String.format("USI download failed with status code: %d",
                        response.code());
                log.error(errorMsg);
                throw new ExternalServiceException("USI Lookup Service", errorMsg);
            }

            // Check response body
            ResponseBody body = response.body();
            if (body == null) {
                throw new ExternalServiceException("USI Lookup Service",
                        "Response body is empty");
            }

            // Parse file
            reader = new BufferedReader(
                    new InputStreamReader(body.byteStream(), StandardCharsets.UTF_8));

            List<HrSuperUsiInfo> usiList = parseUsiCsvFile(reader);

            log.info("Successfully downloaded and parsed {} USI records", usiList.size());
            return usiList;

        } catch (IOException e) {
            String errorMsg = String.format("Network error while downloading USI file: %s",
                    e.getMessage());
            log.error(errorMsg, e);
            throw new ExternalServiceException("USI Lookup Service", errorMsg, e);

        } catch (Exception e) {
            String errorMsg = String.format("Unexpected error during USI download: %s",
                    e.getMessage());
            log.error(errorMsg, e);
            throw new ExternalServiceException("USI Lookup Service", errorMsg, e);

        } finally {
            // Ensure resources are released
            closeQuietly(reader);
            closeQuietly(response);
            shutdownQuietly(client);
        }
    }

    /**
     * Create HTTP client
     */
    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(downloadTimeout, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * Parse CSV file
     */
    private List<HrSuperUsiInfo> parseUsiCsvFile(BufferedReader reader) throws IOException {

        List<HrSuperUsiInfo> usiList = new ArrayList<>();
        String line;
        int lineNumber = 0;
        int errorCount = 0;

        log.debug("Starting CSV parsing");

        try {
            // Skip header line
            reader.readLine();
            lineNumber++;

            // Parse line by line
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    HrSuperUsiInfo usiInfo = parseCsvLine(line);
                    if (usiInfo != null) {
                        usiList.add(usiInfo);
                    }
                } catch (Exception e) {
                    errorCount++;
                    log.warn("Failed to parse line {}: {}", lineNumber, e.getMessage());

                    // If too many errors, stop parsing
                    if (errorCount > 100) {
                        log.error("Too many parse errors ({}), stopping parse", errorCount);
                        throw new ExternalServiceException("USI Lookup Service",
                                "CSV file format error: too many invalid lines");
                    }
                }
            }

            log.info("Parsed {} lines, {} valid records, {} errors",
                    lineNumber, usiList.size(), errorCount);

            return usiList;

        } catch (IOException e) {
            log.error("IO error while parsing CSV at line {}", lineNumber, e);
            throw e;
        }
    }

    /**
     * Parse single CSV line
     */
    private HrSuperUsiInfo parseCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (fields.length < 7) {
            log.debug("Skipping line with insufficient fields: {}", line);
            return null;
        }

        try {
            HrSuperUsiInfo usiInfo = new HrSuperUsiInfo();

            usiInfo.setAbn(cleanField(fields[0]));
            usiInfo.setFundName(cleanField(fields[1]));
            usiInfo.setUsi(cleanField(fields[2]));
            usiInfo.setProductName(cleanField(fields[3]));
            usiInfo.setContributionRestriction(cleanField(fields[4]));

            // Parse dates
            String fromDateStr = cleanField(fields[5]);
            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                usiInfo.setFromDate(LocalDate.parse(fromDateStr, DATE_FORMATTER));
            }

            String toDateStr = cleanField(fields[6]);
            if (toDateStr != null && !toDateStr.isEmpty() && !"NULL".equalsIgnoreCase(toDateStr)) {
                usiInfo.setToDate(LocalDate.parse(toDateStr, DATE_FORMATTER));
            }

            return usiInfo;

        } catch (Exception e) {
            log.debug("Error parsing CSV line: {}", line, e);
            return null;
        }
    }

    /**
     * Clean CSV field (remove quotes and whitespace)
     */
    private String cleanField(String field) {
        if (field == null) {
            return null;
        }
        return field.trim().replaceAll("^\"|\"$", "");
    }

    /**
     * Safely close Reader
     */
    private void closeQuietly(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                log.debug("Error closing reader", e);
            }
        }
    }

    /**
     * Safely close Response
     */
    private void closeQuietly(Response response) {
        if (response != null) {
            try {
                response.close();
            } catch (Exception e) {
                log.debug("Error closing response", e);
            }
        }
    }

    /**
     * Safely shutdown HTTP client
     */
    private void shutdownQuietly(OkHttpClient client) {
        if (client != null) {
            try {
                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
            } catch (Exception e) {
                log.debug("Error shutting down HTTP client", e);
            }
        }
    }
}
