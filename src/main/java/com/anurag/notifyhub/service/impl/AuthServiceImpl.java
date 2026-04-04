package com.anurag.notifyhub.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.notifyhub.dto.request.RegisterRequest;
import com.anurag.notifyhub.dto.response.RegisterResponse;
import com.anurag.notifyhub.exception.EmailAlreadyExistsException;
import com.anurag.notifyhub.model.User;
import com.anurag.notifyhub.repository.UserRepository;
import com.anurag.notifyhub.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  final private UserRepository userRepository;
  final private PasswordEncoder passwordEncoder;

  public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private RegisterResponse mapToUserResponse(User user) {
    RegisterResponse registerResponse = new RegisterResponse();
    registerResponse.setName(user.getName());
    registerResponse.setEmail(user.getEmail());
    registerResponse.setMessage("Registered Successfully");
    registerResponse.setId(user.getId());
    return registerResponse;
  }

  public RegisterResponse registerUser(RegisterRequest registerRequest) {

    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      log.warn("Duplicate email detected | email={}", registerRequest.getEmail());
      throw new EmailAlreadyExistsException("Email already registered");
    }

    User user = new User();
    user.setName(registerRequest.getName());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    User savedUser = userRepository.save(user);
    log.info("User registered | userId={}", savedUser.getId());
    return mapToUserResponse(savedUser);
  }

}
