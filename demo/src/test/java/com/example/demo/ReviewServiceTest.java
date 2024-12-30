package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReviewsPageByProductId() {
        String productId = "123";
        Pageable pageable = mock(Pageable.class);
        Page<Review> reviewPage = new PageImpl<>(Arrays.asList(new Review(), new Review()));

        when(reviewRepository.findByProductId(productId, pageable)).thenReturn(reviewPage);

        Page<Review> result = reviewService.getReviewsPageByProductId(productId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(reviewRepository, times(1)).findByProductId(productId, pageable);
    }

    @Test
    void testAddReview() throws Exception {
        String jwt = "testJwt";
        Review review = new Review();
        User user = new User();
        user.setId("user123");

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.addReview(jwt, review);

        assertNotNull(result);
        verify(userService, times(1)).findUserByJwtToken(jwt);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testUpdateReview_Success() throws Exception {
        String jwt = "testJwt";
        String reviewId = "review123";
        Review existingReview = new Review();
        existingReview.setUserId("user123");
        Review updatedReview = new Review();
        updatedReview.setRating(4);
        updatedReview.setComment("Updated comment");
        User user = new User();
        user.setId("user123");

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);

        Review result = reviewService.updateReview(jwt, reviewId, updatedReview);

        assertNotNull(result);
        assertEquals(4, result.getRating());
        assertEquals("Updated comment", result.getComment());
        verify(reviewRepository, times(1)).save(existingReview);
    }

    @Test
    void testUpdateReview_Failure() throws Exception {
        String jwt = "testJwt";
        String reviewId = "review123";
        Review updatedReview = new Review();
        User user = new User();
        user.setId("user123");

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        Review result = reviewService.updateReview(jwt, reviewId, updatedReview);

        assertNull(result);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testDeleteReview_Success() throws Exception {
        String jwt = "testJwt";
        String reviewId = "review123";
        Review review = new Review();
        review.setUserId("user123");
        User user = new User();
        user.setId("user123");

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        reviewService.deleteReview(jwt, reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testDeleteReview_Failure() throws Exception {
        String jwt = "testJwt";
        String reviewId = "review123";
        User user = new User();
        user.setId("user123");

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        reviewService.deleteReview(jwt, reviewId);

        verify(reviewRepository, never()).deleteById(reviewId);
    }

    @Test
    void testGetAllReviews() {
        Pageable pageable = mock(Pageable.class);
        Page<Review> reviewPage = new PageImpl<>(Arrays.asList(new Review(), new Review()));

        when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);

        Page<Review> result = reviewService.getAllReviews(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(reviewRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDeleteReviewByAdmin() {
        String reviewId = "review123";

        doNothing().when(reviewRepository).deleteById(reviewId);

        reviewService.deleteReviewByAdmin(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }
}
