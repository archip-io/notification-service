package com.archipio.notificationservice.service;

public interface EmailService {

  void sendMessage(String to, String subject, String text);
}
