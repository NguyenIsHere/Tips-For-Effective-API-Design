package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Lấy giỏ hàng của người dùng theo userId
    public Cart getCartByUserId(String userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        return optionalCart.orElse(null); // Trả về null nếu không tìm thấy giỏ hàng
    }

    // Thêm sản phẩm vào giỏ hàng
    public Cart addItemToCart(String userId, CartItem newItem) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart = optionalCart.orElse(new Cart(userId, List.of(newItem))); // Tạo giỏ hàng mới nếu chưa có

        boolean itemExists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(newItem.getProductId()) &&
                item.getColor().equals(newItem.getColor()) &&
                item.getSizeName().equals(newItem.getSizeName())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity()); // Cập nhật số lượng
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            cart.getItems().add(newItem); // Thêm sản phẩm mới vào giỏ nếu chưa có
        }

        return cartRepository.save(cart); // Lưu giỏ hàng
    }

    // Cập nhật thông tin sản phẩm trong giỏ hàng
    public Cart updateCartItem(String userId, CartItem updatedItem) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            for (CartItem item : cart.getItems()) {
                if (item.getProductId().equals(updatedItem.getProductId()) &&
                    item.getColor().equals(updatedItem.getColor()) &&
                    item.getSizeName().equals(updatedItem.getSizeName())) {
                    item.setQuantity(updatedItem.getQuantity()); // Cập nhật số lượng
                    item.setPrice(updatedItem.getPrice()); // Cập nhật giá
                    return cartRepository.save(cart); // Lưu lại giỏ hàng đã cập nhật
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm hoặc giỏ hàng
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public Cart removeItemFromCart(String userId, String productId, String sizeName) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().removeIf(item -> item.getProductId().equals(productId) && item.getSizeName().equals(sizeName));
            return cartRepository.save(cart); // Lưu giỏ hàng sau khi xóa
        }
        return null; // Trả về null nếu không tìm thấy giỏ hàng
    }

    // Xóa toàn bộ giỏ hàng (khi thanh toán hoặc hủy đơn)
    public void clearCart(String userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            cartRepository.save(cart); // Xóa tất cả các sản phẩm trong giỏ hàng
        }
    }
}


