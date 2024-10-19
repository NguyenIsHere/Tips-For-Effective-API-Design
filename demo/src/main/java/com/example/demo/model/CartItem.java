package com.example.demo.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItem {

  private String productId;  // ID của sản phẩm
  private String productName;  // Tên của sản phẩm (lưu để hiển thị)
  private String color;  // Màu sắc của sản phẩm mà người dùng chọn
  private String sizeName;  // Kích thước của sản phẩm (S, M, L, XL,...)
  private int quantity;  // Số lượng sản phẩm người dùng chọn
  private double price; // Giá của sản phẩm với size đã chọn
  private LocalDateTime addedAt;  // Thời gian sản phẩm được thêm vào giỏ hàng

  // Constructors, Getters, and Setters
  public CartItem() {}

  public CartItem(String productId, String productName, String color, String sizeName, int quantity, double price) {
      this.productId = productId;
      this.productName = productName;
      this.color = color;
      this.sizeName = sizeName;
      this.quantity = quantity;
      this.price = price;
      this.addedAt = LocalDateTime.now();  // Ghi lại thời gian thêm vào giỏ hàng
  }
}

