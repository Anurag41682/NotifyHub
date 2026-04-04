package com.anurag.notifyhub.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
  String token;
  String name;
  Long id;
  String email;
}
