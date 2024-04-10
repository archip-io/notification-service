package com.archipio.notificationservice.service.impl;

import com.archipio.notificationservice.client.TemplateServiceClient;
import com.archipio.notificationservice.dto.RenderDto;
import com.archipio.notificationservice.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final TemplateServiceClient templateServiceClient;

  @Value("${spring.mail.username}")
  private String from;

  @Override
  public void sendMessage(String to, String subject, String text) {
    try {
      var mimeMessage = mailSender.createMimeMessage();

      var helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text, true);

      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void sendTemplateMessage(String to, String subject, RenderDto renderDto) {
    var text = templateServiceClient.renderTemplate(renderDto);
    sendMessage(to, subject, text);
  }
}
