package com.example.demo.controller;

import com.example.demo.model.OrderRequest;
import com.example.demo.service.ZaloPayCallbackService;
import com.example.demo.service.ZaloPayCreateOrderService;
import com.example.demo.service.ZaloPayOrderStatusService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment/zalopay")
public class ZaloPayController {

  @Autowired
  private ZaloPayCreateOrderService zaloPayCreateOrderService;

  @PostMapping("/create-order")
  public ResponseEntity<String> createZaloPayOrder(@RequestHeader("Authorization") String jwt,
      @RequestBody OrderRequest orderRequest) throws Exception {
    // Gọi service để tạo đơn hàng và nhận kết quả từ ZaloPay
    String response = zaloPayCreateOrderService.createOrder(jwt, orderRequest);
    return ResponseEntity.ok(response);
  }

  @Autowired
  private ZaloPayOrderStatusService zaloPayOrderStatusService;

  @GetMapping("/order-status")
  public ResponseEntity<Map<String, Object>> getOrderStatus(@RequestHeader("Authorization") String jwt,
      @RequestParam String orderRequestId) throws Exception {
    Map<String, Object> response = zaloPayOrderStatusService.getOrderStatus(orderRequestId);
    return ResponseEntity.ok(response); // Respond with JSON
  }

  @Autowired
  private ZaloPayCallbackService zaloPayCallbackService;

  // Endpoint để xử lý callback từ ZaloPay
  // Mỗi khi test phải dùng ngrok để public IP và cấu hình trong ZaloPay Merchant
  // Sau đó gán đường dẫn vào trường callbackUrl của đơn hàng lúc tạo đơn hàng
  // Ví dụ: "callbackUrl":
  // "https://6925-125-235-233-227.ngrok-free.app/api/payment/zalopay/callback"
  // phần https://6925-125-235-233-227.ngrok-free.app là ngrok cung cấp
  // ta cần thêm /api/payment/zalopay/callback vào phần callbackUrl
  @PostMapping("/callback")
  public ResponseEntity<Map<String, Object>> handleZaloPayCallback(@RequestBody Map<String, Object> postdatajson) {
    Map<String, Object> response = zaloPayCallbackService.handleCallback(postdatajson);
    return ResponseEntity.ok(response);
  }
}
