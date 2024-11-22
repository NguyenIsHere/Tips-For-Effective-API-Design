package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Lấy giỏ hàng của người dùng theo JWT token
    @GetMapping
    public ResponseEntity<Cart> getCartByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        Cart cart = cartService.getCartByJwtToken(jwt); // Sử dụng JWT để lấy cart
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.notFound().build();
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/items")
    public ResponseEntity<Cart> addItemToCart(@RequestHeader("Authorization") String jwt,
            @RequestBody CartItem cartItem) throws Exception {
        Cart updatedCart = cartService.addItemToCart(jwt, cartItem); // Sử dụng JWT để thêm sản phẩm vào cart
        return ResponseEntity.ok(updatedCart);
    }

    // Cập nhật số lượng hoặc thông tin sản phẩm trong giỏ hàng
    @PutMapping("/items")
    public ResponseEntity<Cart> updateCartItem(@RequestHeader("Authorization") String jwt,
            @RequestBody CartItem cartItem) throws Exception {
        Cart updatedCart = cartService.updateCartItem(jwt, cartItem); // Sử dụng JWT để cập nhật sản phẩm trong cart
        return updatedCart != null ? ResponseEntity.ok(updatedCart) : ResponseEntity.notFound().build();
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/items/{productId}/{colorName}/{sizeName}")
    public ResponseEntity<Cart> removeItemFromCart(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String productId,
            @PathVariable String colorName,
            @PathVariable String sizeName) throws Exception {

        Cart updatedCart = cartService.removeItemFromCart(jwt, productId, colorName, sizeName);
        return updatedCart != null ? ResponseEntity.ok(updatedCart) : ResponseEntity.notFound().build();
    }

    // Xóa toàn bộ giỏ hàng (khi thanh toán hoặc hủy đơn)
    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("Authorization") String jwt) throws Exception {
        cartService.clearCart(jwt); // Sử dụng JWT để xóa toàn bộ giỏ hàng
        return ResponseEntity.noContent().build();
    }
}
