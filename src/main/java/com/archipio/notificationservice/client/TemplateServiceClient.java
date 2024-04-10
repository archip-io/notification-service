package com.archipio.notificationservice.client;

import com.archipio.notificationservice.dto.RenderDto;

public interface TemplateServiceClient {

  String renderTemplate(RenderDto renderDto);
}
