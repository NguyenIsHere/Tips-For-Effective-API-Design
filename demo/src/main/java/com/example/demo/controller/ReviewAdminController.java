package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("api/admin/reviews")
public class ReviewAdminController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // Lấy tất cả đánh giá
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // Xóa đánh giá
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReviewByAdmin(reviewId);
        return ResponseEntity.noContent().build();
    }
}
