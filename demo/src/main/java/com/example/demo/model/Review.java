package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;  // MongoDB sử dụng String cho ObjectId

    @Field("product_id")
    private String productId;  // ID của sản phẩm (tham chiếu đến bảng Product)

    @Field("user_id")
    private String userId;  // ID của người dùng (sử dụng từ lớp User)

    private int rating;  // Đánh giá từ 1-5 sao
    private String comment;  // Nội dung đánh giá
    private LocalDateTime reviewDate;  // Thời gian đánh giá

    // Constructor
    public Review(String productId, String userId, int rating, String comment, LocalDateTime reviewDate) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
}
