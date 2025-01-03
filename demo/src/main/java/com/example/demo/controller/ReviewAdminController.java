package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("api/v1/admin/reviews")
public class ReviewAdminController {

    @Autowired
    private ReviewService reviewService;

    // Lấy tất cả đánh giá
    @GetMapping("/all")
    public Page<Review> getAllReviews(@RequestHeader("Authorization") String jwt, Pageable pageable) throws Exception {
        return reviewService.getAllReviews(pageable);
    }

    // Xóa đánh giá
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId, @RequestHeader("Authorization") String jwt)
            throws Exception {
        reviewService.deleteReviewByAdmin(reviewId);
        return ResponseEntity.noContent().build();
    }
}
