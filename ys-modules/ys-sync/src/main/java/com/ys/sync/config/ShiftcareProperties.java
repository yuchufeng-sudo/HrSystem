package com.ys.sync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/16
 */
@Configuration
@ConfigurationProperties(prefix = "shiftcare.api")
@Data
public class ShiftcareProperties {
    private String apiUrl;
    private String username;
    private String password;
}
