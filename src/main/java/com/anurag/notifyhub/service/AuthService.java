package com.anurag.notifyhub.service;

import com.anurag.notifyhub.dto.request.RegisterRequest;
import com.anurag.notifyhub.dto.response.RegisterResponse;

public interface AuthService {
  RegisterResponse registerUser(RegisterRequest registerRequest);
}
