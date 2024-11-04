package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.demo.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByProductId(String productId);

    List<Review> findByUserId(String userId);
    
    Page<Review> findByProductId(String productId, Pageable pageable);

    Page<Review> findAll(Pageable pageable);
}

