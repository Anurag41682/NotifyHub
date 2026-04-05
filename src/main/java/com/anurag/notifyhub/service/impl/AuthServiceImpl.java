package com.anurag.notifyhub.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.notifyhub.dto.request.LoginRequest;
import com.anurag.notifyhub.dto.request.RegisterRequest;
import com.anurag.notifyhub.dto.response.LoginResponse;
import com.anurag.notifyhub.dto.response.RegisterResponse;
import com.anurag.notifyhub.exception.EmailAlreadyExistsException;
import com.anurag.notifyhub.exception.InvalidCredentialsException;
import com.anurag.notifyhub.model.User;
import com.anurag.notifyhub.repository.UserRepository;
import com.anurag.notifyhub.security.JwtUtil;
import com.anurag.notifyhub.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  final private UserRepository userRepository;
  final private PasswordEncoder passwordEncoder;
  final private JwtUtil jwtUtil;

  public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  private RegisterResponse mapToRegisterResponse(User user) {
    RegisterResponse registerResponse = new RegisterResponse();
    registerResponse.setName(user.getName());
    registerResponse.setEmail(user.getEmail());
    registerResponse.setMessage("Registered Successfully");
    registerResponse.setId(user.getId());
    return registerResponse;
  }

  private LoginResponse mapToLoginResponse(String token, User user) {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setEmail(user.getEmail());
    loginResponse.setId(user.getId());
    loginResponse.setName(user.getName());
    loginResponse.setToken(token);
    return loginResponse;
  }

  public RegisterResponse registerUser(RegisterRequest registerRequest) {

    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new EmailAlreadyExistsException("Email already registered");
    }

    User user = new User();
    user.setName(registerRequest.getName());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    User savedUser = userRepository.save(user);
    log.info("User registered | userId={}", savedUser.getId());
    return mapToRegisterResponse(savedUser);
  }

  public LoginResponse loginUser(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password"));
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid Email or Password");
    }
    String token = jwtUtil.generateToken(user.getEmail());
    log.info("Successfully Logged in: email={}", user.getEmail());
    return mapToLoginResponse(token, user);

  }

}
