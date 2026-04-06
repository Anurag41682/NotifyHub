package com.anurag.notifyhub.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {
  private Long id;
  private String name;
  private String email;
  private String message;
}
