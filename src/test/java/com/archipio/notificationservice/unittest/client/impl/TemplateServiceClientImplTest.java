package com.archipio.notificationservice.unittest.client.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.archipio.notificationservice.client.impl.TemplateServiceClientImpl;
import com.archipio.notificationservice.config.RestClientProperties;
import com.archipio.notificationservice.dto.RenderDto;
import com.archipio.notificationservice.exception.RestClientNotFoundException;
import com.archipio.notificationservice.exception.RestClientUrlNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TemplateServiceClientImplTest {

  @Mock private RestTemplate restTemplate;
  @Mock private RestClientProperties restClientProperties;
  @InjectMocks private TemplateServiceClientImpl templateServiceClient;

  @Test
  @Order(1)
  void renderTemplate_whenRestClientNotInitialize_thenThrownRestClientNotFoundException() {
    // Prepare
    when(restClientProperties.getClients()).thenReturn(new HashMap<>());

    // Do
    assertThatExceptionOfType(RestClientNotFoundException.class)
        .isThrownBy(() -> templateServiceClient.renderTemplate(null));
  }

  @Test
  @Order(2)
  void renderTemplate_whenRestClientUrlNotInitialize_thenThrownRestClientUrlNotFoundException() {
    // Prepare
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of("template-service", RestClientProperties.ClientProperties.builder().build()));

    // Do
    assertThatExceptionOfType(RestClientUrlNotFoundException.class)
        .isThrownBy(() -> templateServiceClient.renderTemplate(null));
  }

  @Test
  void renderTemplate_whenCorrectData_thenReturnRenderedText() {
    // Prepare
    final var renderDto = RenderDto.builder().build();
    final var httpEntity = new HttpEntity<>(renderDto);
    final var responseBody = "<h1>Hello</h1>";

    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "template-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://template-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(POST), eq(httpEntity), eq(String.class)))
        .thenReturn(ResponseEntity.ok(responseBody));

    // Do
    var actualResponseBody = templateServiceClient.renderTemplate(renderDto);

    // Check
    verify(restTemplate, only()).exchange(anyString(), eq(POST), eq(httpEntity), eq(String.class));
    assertThat(actualResponseBody).isEqualTo(responseBody);
  }

  @Test
  void renderTemplate_whenIncorrectDataOrUnexpectedError_thenThrownHttpClientErrorException() {
    // Prepare
    final var renderDto = RenderDto.builder().build();
    final var httpEntity = new HttpEntity<>(renderDto);

    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "template-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://template-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(POST), eq(httpEntity), eq(String.class)))
        .thenThrow(new HttpClientErrorException(BAD_REQUEST));

    // Do
    assertThatExceptionOfType(HttpClientErrorException.class)
        .isThrownBy(() -> templateServiceClient.renderTemplate(renderDto));

    // Check
    verify(restTemplate, only()).exchange(anyString(), eq(POST), eq(httpEntity), eq(String.class));
  }
}
