package com.archipio.notificationservice.unittest.amqp.listener;

import com.archipio.notificationservice.amqp.listener.NotificationByEmailListener;
import com.archipio.notificationservice.amqp.message.NotificationEmailMessage;
import com.archipio.notificationservice.dto.RenderDto;
import com.archipio.notificationservice.mapper.RenderMapper;
import com.archipio.notificationservice.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationByEmailListenerTest {

  @Mock private RenderMapper renderMapper;
  @Mock private EmailService emailService;
  @InjectMocks private NotificationByEmailListener notificationByEmailListener;

  @Test
  void handleSendTemplateEmail_thenReceiveMessage_whenSendEmail() {
    // Prepare
    final var to = "user@mail.ru";
    final var subject = "Subject";
    var message =
            NotificationEmailMessage.builder()
                    .to(to)
                    .subject(subject)
                    .build();
    var renderDto = RenderDto.builder().build();

    doReturn(renderDto).when(renderMapper).toDto(message.getTemplate());
    doNothing().when(emailService).sendTemplateMessage(to, subject, renderDto);

    // Do
    notificationByEmailListener.handleSendTemplateEmail(message);

    // Check
    verify(renderMapper, times(1)).toDto(message.getTemplate());
    verify(emailService, times(1)).sendTemplateMessage(to, subject, renderDto);
  }
}
