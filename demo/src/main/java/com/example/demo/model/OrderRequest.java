package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class OrderRequest {
  @Id
  private String orderRequestId; // apptransId sẽ được gán cho orderRequestId
  @Field("user_id")
  private String userId; // ID của người dùng (sử dụng từ lớp User)
  private int amount;
  private String status = "PENDING"; // Trạng thái đơn hàng
  private String item;
  private String bankCode;
  private String description;
  private String embedData;
  private String callbackUrl;
}

