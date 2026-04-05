package com.anurag.notifyhub.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  ResponseEntity<String> emailAlreadyExist(EmailAlreadyExistsException emailAlreadyExistsException) {
    log.warn("Email already exists | message={}", emailAlreadyExistsException.getMessage());
    return ResponseEntity.status(409).body(emailAlreadyExistsException.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<String> methodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException) {
    log.warn("Validation failed | message={}", methodArgumentNotValidException.getFieldErrorCount());
    return ResponseEntity.status(400)
        .body(methodArgumentNotValidException.getBindingResult().getFieldErrors().toString());
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  ResponseEntity<String> invalidCredentials(InvalidCredentialsException invalidCredentialsException) {
    log.warn("Invalid Email or Password");
    return ResponseEntity.status(401).body(invalidCredentialsException.getMessage());
  }

}
