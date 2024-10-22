package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class OrderItem {
    private String productId;  // ID của sản phẩm
    private String productName;  // Tên của sản phẩm (lưu để hiển thị)
    private String colorName;  // Màu sắc của sản phẩm mà người dùng chọn
    private String sizeName;  // Kích thước của sản phẩm (S, M, L, XL,...)
    private int quantity;  // Số lượng sản phẩm người dùng chọn
    private double totalPrice; // Giá của sản phẩm với size đã chọn
}
