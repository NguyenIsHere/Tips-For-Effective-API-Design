package com.example.demo.repository;

import com.example.demo.model.OrderRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrderRequestRepository extends MongoRepository<OrderRequest, String> {
    public List<OrderRequest> findByUserId(String userId);

    public Optional<OrderRequest> findById(String id);
    
    public Page<OrderRequest> findByUserId(String userId, Pageable pageable);
}
