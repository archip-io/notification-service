package com.archipio.notificationservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "rest", ignoreUnknownFields = false)
public class RestClientProperties {

    private Map<String, ClientProperties> clients;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClientProperties {

        private String url;
    }
}
