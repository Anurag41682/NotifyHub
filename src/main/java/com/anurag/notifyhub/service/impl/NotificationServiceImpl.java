package com.anurag.notifyhub.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anurag.notifyhub.dto.request.NotificationRequest;
import com.anurag.notifyhub.dto.response.NotificationResponse;
import com.anurag.notifyhub.exception.NotificationNotFoundException;
import com.anurag.notifyhub.exception.UserNotFoundException;
import com.anurag.notifyhub.model.Notification;
import com.anurag.notifyhub.model.User;
import com.anurag.notifyhub.producer.NotificationProducer;
import com.anurag.notifyhub.repository.NotificationRepository;
import com.anurag.notifyhub.repository.UserRepository;
import com.anurag.notifyhub.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
  final private NotificationRepository notificationRepository;
  final private UserRepository userRepository;
  final private NotificationProducer notificationProducer;

  public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository,
      NotificationProducer notificationProducer) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.notificationProducer = notificationProducer;
  }

  NotificationResponse mapToNotificationResponse(Notification notification) {
    NotificationResponse notificationResponse = new NotificationResponse();
    notificationResponse.setId(notification.getId());
    notificationResponse.setCreatedAt(notification.getCreatedAt());
    notificationResponse.setMessage(notification.getMessage());
    notificationResponse.setRecipientId(notification.getRecipient().getId());
    notificationResponse.setStatus(notification.getStatus());
    notificationResponse.setType(notification.getType());
    notificationResponse.setTitle(notification.getTitle());
    notificationResponse.setSentAt(notification.getSentAt());
    return notificationResponse;
  }

  @Override
  public NotificationResponse createNotification(NotificationRequest notificationRequest) {
    User user = userRepository.findById(notificationRequest.getRecipientId())
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    Notification notification = new Notification();
    notification.setRecipient(user);
    notification.setMessage(notificationRequest.getMessage());
    notification.setTitle(notificationRequest.getTitle());
    notification.setType(notificationRequest.getType());
    Notification savedNotification = notificationRepository.save(notification);
    notificationProducer.sendNotification(savedNotification.getId());
    return mapToNotificationResponse(savedNotification);
  }

  @Override
  public List<NotificationResponse> getNotificationForRecipient(Long recipientId) {

    List<Notification> notificationForRecipient = notificationRepository.findAllByRecipientId(recipientId);

    List<NotificationResponse> response = notificationForRecipient.stream().map(ele -> mapToNotificationResponse(ele))
        .toList();
    return response;
  }

  @Override
  public String deleteNotification(Long id) {
    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
    notificationRepository.delete(notification);
    return "Notification deleted successfully";
  }

  @Override
  public NotificationResponse getNotificationById(Long id) {
    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
    return mapToNotificationResponse(notification);
  }

}
