package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    public List<Order> findByUserId(String userId);

    public Optional<Order> findByOrderId(String orderId);

}
