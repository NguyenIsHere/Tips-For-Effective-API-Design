package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService; // Tiêm ReviewService để xử lý logic nghiệp vụ

    // Tạo đánh giá mới
    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        Review createdReview = reviewService.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview); // Trả về mã 201 khi tạo thành công
    }

    // Lấy tất cả đánh giá cho một sản phẩm
    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable String productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return reviews.isEmpty() 
                ? ResponseEntity.noContent().build() // Trả về mã 204 nếu không có đánh giá
                : ResponseEntity.ok(reviews); // Trả về danh sách đánh giá
    }

    // Cập nhật một đánh giá
    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable String reviewId, @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(reviewId, review);
        return updatedReview != null 
                ? ResponseEntity.ok(updatedReview) // Trả về đánh giá đã cập nhật
                : ResponseEntity.notFound().build(); // Trả về mã 404 nếu không tìm thấy đánh giá
    }

    // Xóa một đánh giá
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String reviewId) {
        if (reviewService.deleteReview(reviewId)) {
            return ResponseEntity.noContent().build(); // Trả về mã 204 nếu xóa thành công
        } else {
            return ResponseEntity.notFound().build(); // Trả về mã 404 nếu không tìm thấy đánh giá
        }
    }
}


