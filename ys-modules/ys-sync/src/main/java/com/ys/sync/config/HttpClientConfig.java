package com.ys.sync.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Frank
 */
@Configuration
public class HttpClientConfig {

    @Bean
    public CloseableHttpClient httpClient() {
        HttpHost proxy = new HttpHost("127.0.0.1", 7890);
        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        return HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }
}
