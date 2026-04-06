package com.anurag.notifyhub.exception;

public class NotificationNotFoundException extends RuntimeException {
  public NotificationNotFoundException(String message) {
    super(message);
  }
}
