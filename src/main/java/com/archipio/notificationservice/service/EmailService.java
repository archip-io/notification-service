package com.archipio.notificationservice.service;

import com.archipio.notificationservice.dto.RenderDto;

public interface EmailService {

  void sendMessage(String to, String subject, String text);

  void sendTemplateMessage(String to, String subject, RenderDto renderDto);
}
