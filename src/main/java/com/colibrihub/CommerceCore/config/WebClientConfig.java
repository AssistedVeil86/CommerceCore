package com.colibrihub.CommerceCore.config;

import com.colibrihub.CommerceCore.Filter.WooCommerceAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient wooCommerceWebClient(
            @Value("${woo.base-url}") String baseUrl,
            @Value("${woo.consumer-key}" ) String consumerKey,
            @Value("${woo.consumer-secret}") String consumerSecret) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(new WooCommerceAuthFilter(consumerKey, consumerSecret))
                .build();
    }
}
