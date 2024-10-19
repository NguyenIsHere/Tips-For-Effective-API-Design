package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(
    collection = "cartItems"
)
public class CartItem {
    @Id
    private String id;
    @DBRef
    private Cart cart;

    @DBRef
    private Product product;

    private int quantity;
    private Long totalPrice;

}
