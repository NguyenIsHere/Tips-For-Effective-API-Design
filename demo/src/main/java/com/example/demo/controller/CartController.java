package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Lấy giỏ hàng của người dùng theo userId
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable String userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable String userId, @RequestBody CartItem cartItem) {
        Cart updatedCart = cartService.addItemToCart(userId, cartItem);
        return ResponseEntity.ok(updatedCart);
    }

    // Cập nhật số lượng hoặc thông tin sản phẩm trong giỏ hàng
    @PutMapping("/{userId}/items")
    public ResponseEntity<Cart> updateCartItem(@PathVariable String userId, @RequestBody CartItem cartItem) {
        Cart updatedCart = cartService.updateCartItem(userId, cartItem);
        return updatedCart != null ? ResponseEntity.ok(updatedCart) : ResponseEntity.notFound().build();
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/{userId}/items/{productId}/{sizeName}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable String userId, @PathVariable String productId, @PathVariable String sizeName) {
        Cart updatedCart = cartService.removeItemFromCart(userId, productId, sizeName);
        return updatedCart != null ? ResponseEntity.ok(updatedCart) : ResponseEntity.notFound().build();
    }

    // Xóa toàn bộ giỏ hàng (khi thanh toán hoặc hủy đơn)
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
