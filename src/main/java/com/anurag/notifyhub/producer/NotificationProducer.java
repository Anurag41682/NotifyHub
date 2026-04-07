package com.anurag.notifyhub.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationProducer {
  private final RabbitTemplate rabbitTemplate;

  public NotificationProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendNotification(Long notificationId) {
    rabbitTemplate.convertAndSend("notification-exchange", "notification-routing-key", notificationId);
    log.info("Notification sent to queue | notificationId={}", notificationId);
  }

}
