package com.anurag.notifyhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anurag.notifyhub.model.Notification;
import com.anurag.notifyhub.model.User;

import java.util.List;

import com.anurag.notifyhub.enums.NotificationStatus;
import com.anurag.notifyhub.enums.NotificationType;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByRecipient(User recipient);

  List<Notification> findAllByRecipientId(Long recipientId);

  List<Notification> findByStatus(NotificationStatus status);

  List<Notification> findByType(NotificationType type);

}
