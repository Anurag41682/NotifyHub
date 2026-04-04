package com.anurag.notifyhub.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {
  Long id;
  String name;
  String email;
  String message;
}
