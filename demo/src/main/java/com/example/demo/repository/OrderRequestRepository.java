package com.example.demo.repository;

import com.example.demo.model.OrderRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRequestRepository extends MongoRepository<OrderRequest, String> {
    public List<OrderRequest> findByUserId(String userId);
    public Optional<OrderRequest> findById(String id);
}
