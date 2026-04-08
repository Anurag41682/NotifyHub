package com.anurag.notifyhub.consumer;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.anurag.notifyhub.enums.NotificationStatus;
import com.anurag.notifyhub.exception.NotificationNotFoundException;
import com.anurag.notifyhub.model.Notification;
import com.anurag.notifyhub.producer.NotificationProducer;
import com.anurag.notifyhub.repository.NotificationRepository;
import com.anurag.notifyhub.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationConsumer {

  final private NotificationRepository notificationRepository;
  final private NotificationProducer notificationProducer;
  final private RabbitTemplate rabbitTemplate;
  final private EmailService emailService;

  public NotificationConsumer(NotificationRepository notificationRepository,
      NotificationProducer notificationProducer, RabbitTemplate rabbitTemplate, EmailService emailService) {
    this.notificationRepository = notificationRepository;
    this.notificationProducer = notificationProducer;
    this.rabbitTemplate = rabbitTemplate;
    this.emailService = emailService;
  }

  @RabbitListener(queues = "notification-queue")
  public void consumeNotification(Long notificationId) {
    log.info("Recieved notification from Queue | notificatioId{}", notificationId);
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + notificationId));

    notification.setStatus(NotificationStatus.PROCESSING);
    notificationRepository.save(notification);
    try {
      // throw new RuntimeException("Simulating failure");
      emailService.sendEmail(notification.getRecipient().getEmail(), notification.getTitle(),
          notification.getMessage());
      log.info("Notification sent");
      notification.setStatus(NotificationStatus.SENT);
      notification.setSentAt(LocalDateTime.now());
      notificationRepository.save(notification);

    } catch (Exception ex) {
      log.error("Notification failed to send | notificationId{}", notificationId, ex);
      if (notification.getRetryCount() < 3) {
        try {
          Thread.sleep((long) Math.pow(2, notification.getRetryCount()) * 1000);
        } catch (InterruptedException exc) {
          Thread.currentThread().interrupt();
          log.error(exc.getMessage());
        }
        notification.setRetryCount(notification.getRetryCount() + 1);
        log.info("Retrying to send notification | count{}", notification.getRetryCount());
        notificationRepository.save(notification);
        notificationProducer.sendNotification(notificationId);
      } else {
        log.error("Notification moved to DLQ | notificationId={}", notificationId);
        rabbitTemplate.convertAndSend("notification-dlq", notificationId);
        notification.setStatus(NotificationStatus.FAILED);
        notificationRepository.save(notification);
      }

    }
  }

}
