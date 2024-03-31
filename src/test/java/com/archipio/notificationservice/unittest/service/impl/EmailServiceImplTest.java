package com.archipio.notificationservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.archipio.notificationservice.service.impl.EmailServiceImpl;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

  @Mock private JavaMailSender mailSender;

  @InjectMocks private EmailServiceImpl emailService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(emailService, "from", "from@mail.ru");
  }

  @Test
  void sendMessage_whenMimeMessageIsValid_thenSendMessage() {
    // Prepare
    final var to = "to@mail.ru";
    final var subject = "Hello";
    final var text = "World!";
    var mimeMessage = new MimeMessage((Session) null);

    doReturn(mimeMessage).when(mailSender).createMimeMessage();
    doNothing().when(mailSender).send(mimeMessage);

    // When
    emailService.sendMessage(to, subject, text);

    // Then
    verify(mailSender, times(1)).createMimeMessage();
    verify(mailSender, times(1)).send(mimeMessage);
  }

  @Test
  void sendMessage_whenMimeMessageIsInvalid_thenThrownMailException() {
    // Prepare
    final var to = "to@mail.ru";
    final var subject = "Hello";
    final var text = "World!";
    var mimeMessage = new MimeMessage((Session) null);

    doReturn(mimeMessage).when(mailSender).createMimeMessage();
    doThrow(MailSendException.class).when(mailSender).send(mimeMessage);

    // When
    assertThatExceptionOfType(MailException.class)
        .isThrownBy(() -> emailService.sendMessage(to, subject, text));

    // Then
    verify(mailSender, times(1)).createMimeMessage();
    verify(mailSender, times(1)).send(mimeMessage);
  }
}
