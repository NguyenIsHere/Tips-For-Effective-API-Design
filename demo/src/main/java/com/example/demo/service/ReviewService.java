package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.model.User; // Import model User nếu cần thiết

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;  // Để lấy thông tin người dùng từ JWT

    // Lấy danh sách đánh giá của sản phẩm theo productId
    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId);
    }

    // Thêm đánh giá mới
    public Review addReview(String jwt, Review review) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        review.setUserId(user.getId());  // Thiết lập userId cho đánh giá
        review.setReviewDate(LocalDateTime.now());  // Thiết lập thời gian đánh giá
        return reviewRepository.save(review);
    }

    // Cập nhật đánh giá
    public Review updateReview(String jwt, String reviewId, Review updatedReview) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent() && optionalReview.get().getUserId().equals(user.getId())) {
            Review existingReview = optionalReview.get();
            existingReview.setRating(updatedReview.getRating());
            existingReview.setComment(updatedReview.getComment());
            return reviewRepository.save(existingReview);
        }
        return null; // Trả về null nếu không tìm thấy đánh giá hoặc không phải của người dùng
    }

    // Xóa đánh giá
    public void deleteReview(String jwt, String reviewId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent() && optionalReview.get().getUserId().equals(user.getId())) {
            reviewRepository.deleteById(reviewId);  // Xóa đánh giá của người dùng
        }
    }

    // Lấy tất cả đánh giá (Admin)
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Xóa đánh giá (Admin)
    public void deleteReviewByAdmin(String reviewId) {
        reviewRepository.deleteById(reviewId);  // Admin có thể xóa bất kỳ đánh giá nào
    }
}
