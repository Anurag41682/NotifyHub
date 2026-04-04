package com.anurag.notifyhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
  @NotBlank
  String name;
  @NotBlank
  @Email
  String email;
  @NotBlank
  String password;
}
