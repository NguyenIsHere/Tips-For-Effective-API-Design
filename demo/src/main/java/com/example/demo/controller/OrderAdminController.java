package com.example.demo.controller;

import com.example.demo.model.OrderRequest;
import com.example.demo.service.OrderRequestService;
import com.example.demo.service.ZaloPayOrderStatusService;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
public class OrderAdminController {

  @Autowired
  private OrderRequestService orderRequestService;

  // Method để lấy tất cả đơn hàng
  @GetMapping
  public ResponseEntity<List<OrderRequest>> getAllOrders(@RequestHeader("Authorization") String jwt)
      throws Exception {
    List<OrderRequest> orderRequest = orderRequestService.getAllOrders();
    return ResponseEntity.ok(orderRequest);
  }

  @Autowired
  private ZaloPayOrderStatusService zaloPayOrderStatusService;

  // Method để lấy trạng thái đơn hàng thủ công khi callback bị lỗi
  @GetMapping("/order-status")
  public ResponseEntity<Map<String, Object>> getOrderStatus(@RequestHeader("Authorization") String jwt,
      @RequestParam String orderRequestId) throws Exception {
    Map<String, Object> response = zaloPayOrderStatusService.getOrderStatus(orderRequestId);
    return ResponseEntity.ok(response); // Respond with JSON
  }

  // Method lấy 1 đơn hàng
  @GetMapping("/{orderRequestId}")
  public ResponseEntity<OrderRequest> getOrderById(@RequestHeader("Authorization") String jwt,
      @PathVariable String orderRequestId)
      throws Exception {
    OrderRequest orderRequest = orderRequestService.getOrderById(orderRequestId);
    return ResponseEntity.ok(orderRequest);
  }

  // Method để cập nhật trạng thái đơn hàng
  @PatchMapping("/update-order/{orderRequestId}")
  public ResponseEntity<String> updateOrderStatus(@RequestHeader("Authorization") String jwt,
      @PathVariable String orderRequestId,
      @RequestBody Map<String, Object> statusUpdate) throws Exception {
    String response = orderRequestService.updateOrderStatus(orderRequestId, statusUpdate);
    return ResponseEntity.ok(response);
  }

  // Method để sửa đơn hàng
  @PutMapping("/edit-order/{orderRequestId}")
  public ResponseEntity<String> editOrder(@RequestHeader("Authorization") String jwt,
      @PathVariable String orderRequestId,
      @RequestBody OrderRequest orderRequest) throws Exception {
    String response = orderRequestService.editOrder(orderRequestId, orderRequest);
    return ResponseEntity.ok(response);
  }

  // Method để xóa đơn hàng
  @DeleteMapping("/delete-order/{orderRequestId}")
  public ResponseEntity<String> deleteOrder(@RequestHeader("Authorization") String jwt,
      @PathVariable String orderRequestId) throws Exception {
    String response = orderRequestService.deleteOrder(orderRequestId);
    return ResponseEntity.ok(response);
  }
}
