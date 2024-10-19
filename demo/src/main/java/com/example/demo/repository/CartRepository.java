package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Cart;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
