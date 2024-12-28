package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
    // Tuỳ vào logic của bạn, có thể log ra file, console, ...
    // Rồi trả về JSON ngắn gọn
    String shortMessage = "Đã xảy ra lỗi: " + ex.getMessage();

    // Có thể trả về dưới dạng Map<String, String> hoặc một DTO lỗi tuỳ ý
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("error", shortMessage);

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR) // Hoặc HttpStatus.BAD_REQUEST tuỳ trường hợp
        .body(responseBody);
  }

  // Nếu bạn có custom exception, có thể tạo thêm @ExceptionHandler tương ứng
  // @ExceptionHandler(CustomNotFoundException.class)
  // public ResponseEntity<Object> handleCustomNotFound(CustomNotFoundException
  // ex) { ... }

}
