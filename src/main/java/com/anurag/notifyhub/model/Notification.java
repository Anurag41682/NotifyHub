package com.anurag.notifyhub.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.anurag.notifyhub.enums.NotificationStatus;
import com.anurag.notifyhub.enums.NotificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "notifications")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String title;

  @NotNull
  private String message;

  @NotNull
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Enumerated(EnumType.STRING)
  private NotificationStatus status = NotificationStatus.PENDING;

  @CreationTimestamp
  private LocalDateTime createdAt;

  private LocalDateTime sentAt;

  private Long retryCount = 0L;

  @ManyToOne
  @JoinColumn(name = "recipient_id")
  private User recipient;

}
