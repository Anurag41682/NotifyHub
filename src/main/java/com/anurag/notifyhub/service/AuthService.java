package com.anurag.notifyhub.service;

import com.anurag.notifyhub.dto.request.LoginRequest;
import com.anurag.notifyhub.dto.request.RegisterRequest;
import com.anurag.notifyhub.dto.response.LoginResponse;
import com.anurag.notifyhub.dto.response.RegisterResponse;

public interface AuthService {
  RegisterResponse registerUser(RegisterRequest registerRequest);

  LoginResponse loginUser(LoginRequest loginRequest);
}
