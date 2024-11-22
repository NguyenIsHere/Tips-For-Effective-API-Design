package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;

@RestController
@RequestMapping("api/v1/reviews")
public class ReviewUserController {

    @Autowired
    private ReviewService reviewService;

    // Lấy đánh giá với phân trang
    @GetMapping("/{productId}")
    public Page<Review> getReviews(@PathVariable String productId, Pageable pageable) {
        return reviewService.getReviewsPageByProductId(productId, pageable);
    }

    // Thêm đánh giá mới
    @PostMapping
    public ResponseEntity<Review> addReview(@RequestHeader("Authorization") String jwt, @RequestBody Review review)
            throws Exception {
        Review createdReview = reviewService.addReview(jwt, review);
        return ResponseEntity.ok(createdReview);
    }

    // Cập nhật đánh giá
    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestHeader("Authorization") String jwt,
            @PathVariable String reviewId, @RequestBody Review review) throws Exception {
        Review updatedReview = reviewService.updateReview(jwt, reviewId, review);
        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
    }

    // Xóa đánh giá
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@RequestHeader("Authorization") String jwt, @PathVariable String reviewId)
            throws Exception {
        reviewService.deleteReview(jwt, reviewId);
        return ResponseEntity.noContent().build();
    }
}
