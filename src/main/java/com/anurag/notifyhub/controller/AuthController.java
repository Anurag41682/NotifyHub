package com.anurag.notifyhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.notifyhub.dto.request.LoginRequest;
import com.anurag.notifyhub.dto.request.RegisterRequest;
import com.anurag.notifyhub.dto.response.LoginResponse;
import com.anurag.notifyhub.dto.response.RegisterResponse;
import com.anurag.notifyhub.service.AuthService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
  final private AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
    log.info("Register Request Recieved | email={}", registerRequest.getEmail());
    RegisterResponse registerResponse = authService.registerUser(registerRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
  }

  @PostMapping("/login")
  ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = authService.loginUser(loginRequest);
    return ResponseEntity.status(200).body(loginResponse);
  }

}
