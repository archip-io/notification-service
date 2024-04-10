package com.archipio.notificationservice.controller.api.v0;

import static com.archipio.notificationservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.notificationservice.util.ApiUtils.EMAIL_PREFIX;
import static com.archipio.notificationservice.util.ApiUtils.SEND_EMAIL_SUFFIX;
import static com.archipio.notificationservice.util.ApiUtils.SEND_EMAIL_TEMPLATE_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.notificationservice.dto.RenderDto;
import com.archipio.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX + EMAIL_PREFIX)
public class EmailController {

  private final EmailService emailService;

  @PreAuthorize("hasAuthority('SEND_MAIL')")
  @PostMapping(
          value = SEND_EMAIL_SUFFIX,
          consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.TEXT_HTML_VALUE})
  public ResponseEntity<Void> sendEmail(
          @RequestParam("to") String to,
          @RequestParam("subject") String subject,
          @RequestBody String body) {
    emailService.sendMessage(to, subject, body);
    return ResponseEntity.status(OK).build();
  }

  @PreAuthorize("hasAuthority('SEND_MAIL')")
  @PostMapping(
          value = SEND_EMAIL_TEMPLATE_SUFFIX,
          consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> sendTemplateEmail(
          @RequestParam("to") String to,
          @RequestParam("subject") String subject,
          @RequestBody RenderDto renderDto) {
    emailService.sendTemplateMessage(to, subject, renderDto);
    return ResponseEntity.status(OK).build();
  }
}
