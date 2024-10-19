package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository; // Repository để tương tác với MongoDB

    // Tạo đánh giá mới
    public Review createReview(Review review) {
        review.setReviewDate(LocalDateTime.now()); // Đặt thời gian đánh giá hiện tại
        return reviewRepository.save(review); // Lưu đánh giá vào MongoDB
    }

    // Lấy tất cả đánh giá cho một sản phẩm
    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId); // Tìm kiếm đánh giá theo ID sản phẩm
    }

    // Cập nhật một đánh giá
    public Review updateReview(String reviewId, Review updatedReview) {
        Optional<Review> existingReview = reviewRepository.findById(reviewId); // Tìm đánh giá theo ID
        if (existingReview.isPresent()) {
            // Cập nhật thông tin đánh giá
            Review review = existingReview.get();
            review.setRating(updatedReview.getRating());
            review.setComment(updatedReview.getComment());
            review.setReviewDate(LocalDateTime.now()); // Cập nhật thời gian đánh giá
            return reviewRepository.save(review); // Lưu đánh giá đã cập nhật
        }
        return null; // Trả về null nếu không tìm thấy đánh giá
    }

    // Xóa một đánh giá
    public boolean deleteReview(String reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId); // Xóa đánh giá theo ID
            return true; // Trả về true nếu xóa thành công
        }
        return false; // Trả về false nếu không tìm thấy đánh giá
    }
}


