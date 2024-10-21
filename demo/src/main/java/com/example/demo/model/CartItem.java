package com.example.demo.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItem {

  private String productId;  // ID của sản phẩm
  private String productName;  // Tên của sản phẩm (lưu để hiển thị)
  private String colorName;  // Màu sắc của sản phẩm mà người dùng chọn
  private String sizeName;  // Kích thước của sản phẩm (S, M, L, XL,...)
  private int quantity;  // Số lượng sản phẩm người dùng chọn
  private double totalPrice; // Giá của sản phẩm với size đã chọn
  private LocalDateTime addedAt;  // Thời gian sản phẩm được thêm vào giỏ hàng

  // Constructors
  public CartItem() {}

  public CartItem(String productId, String productName, String colorName, String sizeName, int quantity, double price) {
      this.productId = productId;
      this.productName = productName;
      this.colorName = colorName;
      this.sizeName = sizeName;
      this.quantity = quantity;
      this.totalPrice = price;
      this.addedAt = LocalDateTime.now();  // Ghi lại thời gian thêm vào giỏ hàng
  }
}

