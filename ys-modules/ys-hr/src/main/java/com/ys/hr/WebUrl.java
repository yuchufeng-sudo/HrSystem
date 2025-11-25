package com.ys.hr;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/16
 */
@Configuration
@ConfigurationProperties(prefix = "web-url")
@Data
public class WebUrl {
    private String url;
}
