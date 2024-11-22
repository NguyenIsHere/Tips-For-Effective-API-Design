package com.example.demo.controller;

import com.example.demo.model.OrderRequest;
import com.example.demo.model.User;
import com.example.demo.service.OrderRequestService;
import com.example.demo.repository.OrderRequestRepository;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderUserController {

  @Autowired
  private OrderRequestRepository orderRequestRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private OrderRequestService orderRequestService;

  // Method lây đơn hàng phân trang
  @GetMapping("/my-orders")
  public Page<OrderRequest> getOrderPage(@RequestHeader("Authorization") String jwt, Pageable pageable)
      throws Exception {
    User user = userService.findUserByJwtToken(jwt);
    return orderRequestRepository.findByUserId(user.getId(), pageable);
  }

  // Method để hủy đơn hàng
  @DeleteMapping("/cancel/{orderRequestId}")
  public ResponseEntity<String> deleteOrder(@RequestHeader("Authorization") String jwt,
      @PathVariable String orderRequestId) throws Exception {
    String response = orderRequestService.deleteOrder(orderRequestId);
    return ResponseEntity.ok(response);
  }
}
