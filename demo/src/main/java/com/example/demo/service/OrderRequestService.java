package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.OrderRequest;
import com.example.demo.repository.OrderRequestRepository;

import java.util.Optional;
import java.util.Map;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class OrderRequestService {

  @Autowired
  private OrderRequestRepository orderRequestRepository;

  // Lấy toàn bộ đơn hàng
  public List<OrderRequest> getAllOrders() {
    return orderRequestRepository.findAll();
  }

  // Lấy 1 đơn hàng
  public OrderRequest getOrderById(String orderRequestId) {
    Optional<OrderRequest> optionalOrderRequest = orderRequestRepository.findById(orderRequestId);
    if (optionalOrderRequest.isPresent()) {
      return optionalOrderRequest.get();
    } else {
      return null;
    }
  }

  // Cập nhật trạng thái đơn hàng
  public String updateOrderStatus(String orderRequestId, Map<String, Object> statusUpdate) {
    Optional<OrderRequest> optionalOrderRequest = orderRequestRepository.findById(orderRequestId);
    if (optionalOrderRequest.isPresent()) {
      OrderRequest orderRequest = optionalOrderRequest.get();
      orderRequest.setStatus(statusUpdate.get("status").toString());
      orderRequestRepository.save(orderRequest);
    }

    return "Order status updated successfully";
  }

  // Sửa đơn hàng
  public String editOrder(String orderRequestId, OrderRequest orderRequest) {
      Optional<OrderRequest> optionalOrderRequest = orderRequestRepository.findById(orderRequestId);
      if (optionalOrderRequest.isPresent()) {
          OrderRequest existingOrderRequest = optionalOrderRequest.get();
          existingOrderRequest.setUserId(orderRequest.getUserId());
          existingOrderRequest.setAmount(orderRequest.getAmount());
          existingOrderRequest.setStatus(orderRequest.getStatus());
          existingOrderRequest.setItem(orderRequest.getItem());
          existingOrderRequest.setBankCode(orderRequest.getBankCode());
          existingOrderRequest.setDescription(orderRequest.getDescription());
          existingOrderRequest.setEmbedData(orderRequest.getEmbedData());
          existingOrderRequest.setCallbackUrl(orderRequest.getCallbackUrl());
          orderRequestRepository.save(existingOrderRequest);
          return "Order updated successfully";
      } else {
          return "Order not found";
      }
  }

  // Xóa đơn hàng
  public String deleteOrder(String orderRequestId) {
    orderRequestRepository.deleteById(orderRequestId);
    return "Order status deleted successfully";
  }
  
  // Lấy danh sách đơn hàng của người dùng theo trang
  public Page<OrderRequest> getOrdersByUserId(String userId, Pageable pageable) {
    return orderRequestRepository.findByUserId(userId, pageable);
  }

}
