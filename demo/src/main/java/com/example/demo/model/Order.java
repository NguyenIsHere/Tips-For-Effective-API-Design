package com.example.demo.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document("orders")
public class Order {
    @Id
    private String orderId;
    @Field("user_id")
    private String userId;  // ID của người dùng sở hữu giỏ hàng (sử dụng từ lớp User)
    private List<OrderItem> orderItems = new ArrayList<>();
    private Address deliveryAddress;
    private Date createdAt;
    private String status;
    private double totalPrice;
}
