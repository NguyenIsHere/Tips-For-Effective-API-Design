package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.ArrayList;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;  // ID của giỏ hàng

    @Field("user_id")
    private String userId;  // ID của người dùng sở hữu giỏ hàng (sử dụng từ lớp User)

    private List<CartItem> items;  // Danh sách các sản phẩm trong giỏ hàng

    // Constructor không tham số
    public Cart() {
        this.items = new ArrayList<>(); // Khởi tạo danh sách giỏ hàng rỗng
    }

    // Constructor với tham số
    public Cart(String userId, List<CartItem> items) {
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>(); // Khởi tạo danh sách giỏ hàng
    }

    // Phương thức thêm sản phẩm vào giỏ hàng
    public void addItem(CartItem item) {
        this.items.add(item);
    }

    // Phương thức xóa sản phẩm khỏi giỏ hàng
    public void removeItem(CartItem item) {
        this.items.remove(item);
    }

    // Phương thức cập nhật sản phẩm trong giỏ hàng
    public void updateItem(CartItem updatedItem) {
        for (CartItem item : items) {
            if (item.getProductId().equals(updatedItem.getProductId())
                    && item.getColorName().equals(updatedItem.getColorName())
                    && item.getSizeName().equals(updatedItem.getSizeName())) {
                item.setQuantity(updatedItem.getQuantity());
                item.setTotalPrice(updatedItem.getTotalPrice());
                break;
            }
        }
    }
}

