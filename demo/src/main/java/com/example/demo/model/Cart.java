package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;  // ID của giỏ hàng

    @Field("user_id")
    private String userId;  // ID của người dùng sở hữu giỏ hàng

    private List<CartItem> items;  // Danh sách các sản phẩm trong giỏ hàng

    // Constructors, Getters, and Setters
    public Cart() {}

    public Cart(String userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items;
    }
}

