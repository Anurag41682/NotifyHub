package com.anurag.notifyhub.dto.response;

import java.time.LocalDateTime;

import com.anurag.notifyhub.enums.NotificationStatus;
import com.anurag.notifyhub.enums.NotificationType;

import lombok.Data;

@Data
public class NotificationResponse {
  private String title;
  private String message;
  private Long id;
  private NotificationStatus status;
  private NotificationType type;
  private Long recipientId;
  private LocalDateTime createdAt;
  private LocalDateTime sentAt;
}
