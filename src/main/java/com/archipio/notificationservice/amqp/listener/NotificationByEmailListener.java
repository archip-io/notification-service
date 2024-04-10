package com.archipio.notificationservice.amqp.listener;

import com.archipio.notificationservice.amqp.message.NotificationEmailMessage;
import com.archipio.notificationservice.mapper.RenderMapper;
import com.archipio.notificationservice.service.EmailService;
import com.archipio.notificationservice.util.AMQPUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationByEmailListener {

  private final EmailService emailService;
  private final RenderMapper renderMapper;

  @RabbitListener(queues = {AMQPUtils.NOTIFICATION_BY_EMAIL_QUEUE_NAME})
  public void handleSendTemplateEmail(NotificationEmailMessage message) {
    var renderDto = renderMapper.toDto(message.getTemplate());
    emailService.sendTemplateMessage(message.getTo(), message.getSubject(), renderDto);
  }
}
