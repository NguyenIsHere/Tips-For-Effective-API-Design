package com.example.demo.controller;

import com.example.demo.model.OrderRequest;
import com.example.demo.model.User;
import com.example.demo.service.OrderRequestService;
import com.example.demo.repository.OrderRequestRepository;
import com.example.demo.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderUserController {

  @Autowired
  private OrderRequestRepository orderRequestRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private OrderRequestService orderRequestService;

    // Xem các đơn hàng của mình
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderRequest>> getOrder(@RequestHeader("Authorization") String jwt) throws Exception {
      User user = userService.findUserByJwtToken(jwt);
      List<OrderRequest> orderRequest = orderRequestRepository.findByUserId(user.getId());
      return ResponseEntity.ok(orderRequest);
    }

    // Method để hủy đơn hàng
    @DeleteMapping("/cancel/{orderRequestId}")
    public ResponseEntity<String> deleteOrder(@RequestHeader("Authorization") String jwt, @PathVariable String orderRequestId) throws Exception {
        String response = orderRequestService.deleteOrder(orderRequestId);
        return ResponseEntity.ok(response);
    }
}
