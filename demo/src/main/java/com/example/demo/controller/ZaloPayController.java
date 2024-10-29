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
@RequestMapping("/api/payment/zalopay")
public class ZaloPayController {

    @Autowired
    private ZaloPayCreateOrderService zaloPayCreateOrderService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createZaloPayOrder(@RequestHeader("Authorization") String jwt, @RequestBody OrderRequest orderRequest) throws Exception {
      // Gọi service để tạo đơn hàng và nhận kết quả từ ZaloPay
      String response = zaloPayCreateOrderService.createOrder(jwt, orderRequest);
      return ResponseEntity.ok(response);
    }
    
    @Autowired
    private ZaloPayOrderStatusService orderStatusService;

    @GetMapping("/order-status")
    public ResponseEntity<Map<String, Object>> getOrderStatus(@RequestParam String appTransId) {
      Map<String, Object> response = orderStatusService.getOrderStatus(appTransId);
      return ResponseEntity.ok(response); // Respond with JSON
    }

    @Autowired
    private ZaloPayCallbackService zaloPayCallbackService;
    
    // Endpoint để xử lý callback từ ZaloPay
    @PostMapping("/callback")
    public ResponseEntity<Map<String, Object>> handleZaloPayCallback(@RequestBody Map<String, Object> postdatajson) {
        Map<String, Object> response = zaloPayCallbackService.handleCallback(postdatajson);
        return ResponseEntity.ok(response);
    }
}
