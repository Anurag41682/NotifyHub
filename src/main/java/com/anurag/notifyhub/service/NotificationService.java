package com.anurag.notifyhub.service;

import java.util.List;

import com.anurag.notifyhub.dto.request.NotificationRequest;
import com.anurag.notifyhub.dto.response.NotificationResponse;

public interface NotificationService {
  NotificationResponse createNotification(NotificationRequest notificationRequest, String idempotencyKey);

  List<NotificationResponse> getNotificationForRecipient(Long recipientId);

  String deleteNotification(Long id);

  NotificationResponse getNotificationById(Long id);

}
