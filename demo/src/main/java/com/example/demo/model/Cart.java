package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(
   collection = "carts"
)
public class Cart {
    @Id
    private String id;
    @DBRef
    private User user;
    private long total;

    @DBRef
    private List<CartItem> cartItems = new ArrayList<>();
}

