package com.archipio.notificationservice.exception;

public class RestClientNotFoundException extends RuntimeException {

  public RestClientNotFoundException(String message) {
    super(message);
  }
}
