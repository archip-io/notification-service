package com.archipio.notificationservice.client.impl;

import com.archipio.notificationservice.client.TemplateServiceClient;
import com.archipio.notificationservice.config.RestClientProperties;
import com.archipio.notificationservice.dto.RenderDto;
import com.archipio.notificationservice.exception.RestClientNotFoundException;
import com.archipio.notificationservice.exception.RestClientUrlNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.POST;

@Service
@RequiredArgsConstructor
public class TemplateServiceClientImpl implements TemplateServiceClient {

  private static final String TEMPLATE_SERVICE_NAME = "template-service";
  private static final String RENDER_TEMPLATE_PATH = "/sys/v0/template/render";

  private static String userServiceUrl;

  private final RestTemplate restTemplate;
  private final RestClientProperties restClientProperties;

  @Override
  public String renderTemplate(RenderDto renderDto) {
    var uri =
            UriComponentsBuilder.fromHttpUrl(getUserServiceUrl())
                    .path(RENDER_TEMPLATE_PATH)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUriString();
    var body = new HttpEntity<>(renderDto);
    return restTemplate.exchange(uri, POST, body, String.class).getBody();
  }

  private String getUserServiceUrl() {
    if (userServiceUrl == null) {
      var clientProperties = restClientProperties.getClients().get(TEMPLATE_SERVICE_NAME);
      if (clientProperties == null) {
        throw new RestClientNotFoundException(format("Client %s is null", TEMPLATE_SERVICE_NAME));
      }

      userServiceUrl = clientProperties.getUrl();
      if (userServiceUrl == null) {
        throw new RestClientUrlNotFoundException(
                format("Client %s URL is null", TEMPLATE_SERVICE_NAME));
      }
    }
    return userServiceUrl;
  }
}
