package com.anurag.notifyhub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.notifyhub.dto.request.NotificationRequest;
import com.anurag.notifyhub.dto.response.NotificationResponse;
import com.anurag.notifyhub.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

  final private NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping
  public ResponseEntity<NotificationResponse> createNotification(
      @Valid @RequestBody NotificationRequest notificationRequest) {
    NotificationResponse response = notificationService.createNotification(notificationRequest);
    return ResponseEntity.status(201).body(response);
  }

  @GetMapping("/recipient/{recipientId}")
  public ResponseEntity<List<NotificationResponse>> getNotificationForRecipient(
      @PathVariable Long recipientId) {
    List<NotificationResponse> responses = notificationService.getNotificationForRecipient(recipientId);
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
    NotificationResponse response = notificationService.getNotificationById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteNotificationById(@PathVariable Long id) {
    String response = notificationService.deleteNotification(id);
    return ResponseEntity.status(200).body(response);
  }

}
