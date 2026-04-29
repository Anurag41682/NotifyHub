package com.anurag.notifyhub.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/")
  public Map<String, Object> home() {
    return Map.of(
        "service", "NotifyHub API",
        "version", "1.0.0",
        "status", "running",
        "description", "Async notification service with retry, DLQ, and idempotency",
        "documentation", "https://github.com/Anurag41682/NotifyHub#readme",
        "endpoints", Map.of(
            "register", "POST /api/auth/register",
            "login", "POST /api/auth/login",
            "createNotification", "POST /api/notifications (requires JWT + Idempotency-Key)",
            "getNotificationsByRecipient", "GET /api/notifications/recipient/{recipientId}",
            "getNotificationById", "GET /api/notifications/{id}",
            "deleteNotification", "DELETE /api/notifications/{id}"));
  }
}