package com.anurag.notifyhub.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @NotNull
  @Column(nullable = false)
  String name;
  @NotNull
  @Column(unique = true, nullable = false)
  String email;
  @NotNull
  @Column(nullable = false)
  String password;

  @CreationTimestamp
  LocalDateTime createdAt;

}
