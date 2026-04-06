package com.anurag.notifyhub.dto.request;

import com.anurag.notifyhub.enums.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
  @NotBlank
  private String title;
  @NotNull
  private NotificationType type;
  @NotBlank
  private String message;
  @NotNull
  private Long recipientId;
}
