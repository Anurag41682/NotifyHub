package com.anurag.notifyhub.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
  private String token;
  private String name;
  private Long id;
  private String email;
}
