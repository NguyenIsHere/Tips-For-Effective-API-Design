package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.model.User; // Import model User nếu cần thiết

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService; // Để lấy thông tin người dùng từ JWT

    // Lấy danh sách đánh giá của sản phẩm theo productId với phân trang
    public Page<Review> getReviewsPageByProductId(String productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }

    // Thêm đánh giá mới
    public Review addReview(String jwt, Review review) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Lấy thông tin người dùng từ JWT
        review.setUserId(user.getId()); // Thiết lập userId cho đánh giá
        review.setReviewDate(LocalDateTime.now()); // Thiết lập thời gian đánh giá
        return reviewRepository.save(review);
    }

    public Review updateReview(String jwt, String reviewId, Review updatedReview) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent() && optionalReview.get().getUserId().equals(user.getId())) {
            Review existingReview = optionalReview.get();

            // Chỉ cập nhật rating nếu updatedReview.rating != null
            if (updatedReview.getRating() != null) {
                existingReview.setRating(updatedReview.getRating());
            }

            // Chỉ cập nhật comment nếu updatedReview.comment != null
            if (updatedReview.getComment() != null) {
                existingReview.setComment(updatedReview.getComment());
            }

            return reviewRepository.save(existingReview);
        }
        return null;
    }

    // Xóa đánh giá
    public void deleteReview(String jwt, String reviewId) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Lấy thông tin người dùng từ JWT
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isPresent() && optionalReview.get().getUserId().equals(user.getId())) {
            reviewRepository.deleteById(reviewId); // Xóa đánh giá của người dùng
        }
    }

    // Lấy tất cả đánh giá (Admin)
    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    // Xóa đánh giá (Admin)
    public void deleteReviewByAdmin(String reviewId) {
        reviewRepository.deleteById(reviewId); // Admin có thể xóa bất kỳ đánh giá nào
    }
}
