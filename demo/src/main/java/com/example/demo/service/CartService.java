package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.ArrayList;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.repository.CartRepository;
import com.example.demo.model.User;
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;  // Để lấy thông tin người dùng từ JWT

    @Autowired
    private ProductRepository productRepository;

    // Lấy giỏ hàng của người dùng theo JWT token
    public Cart getCartByJwtToken(String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt); // Lấy thông tin người dùng từ JWT
        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId()); // Sử dụng userId
        return optionalCart.orElse(null); // Trả về null nếu không tìm thấy giỏ hàng
    }
    
    // Thêm sản phẩm vào giỏ hàng
    public Cart addItemToCart(String jwt, CartItem newItem) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());
        Product product = productRepository.findByName(newItem.getProductName());
        Cart cart = optionalCart.orElse(new Cart(user.getId(), new ArrayList<>())); // Tạo giỏ hàng mới nếu chưa có 

        // Tìm giá của sản phẩm với colorName và sizeName từ newItem
        Optional<Double> optionalPrice = product.getColors().stream()
            .filter(color -> color.getColorName().equals(newItem.getColorName()))
            .flatMap(color -> color.getSizes().stream())
            .filter(size -> size.getSizeName().equals(newItem.getSizeName()))
            .map(Size::getPrice)
            .findFirst();

        // Kiểm tra nếu không tìm thấy giá của sản phẩm
        if (optionalPrice.isEmpty()) {
            throw new Exception("Price not found for specified color and size");
        }
        
        double price = optionalPrice.get();
        newItem.setTotalPrice(price);  // Cập nhật giá cho CartItem mới

        // Đảm bảo rằng newItem được khởi tạo với thời gian hiện tại
        if (newItem.getAddedAt() == null) {
            newItem.setAddedAt(LocalDateTime.now());
        }

        boolean itemExists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(newItem.getProductId()) &&
                item.getColorName().equals(newItem.getColorName()) &&
                item.getSizeName().equals(newItem.getSizeName())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity()); // Cập nhật số lượng
                item.setTotalPrice(item.getQuantity() * price); // Cập nhật tổng giá
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
    public Cart updateCartItem(String jwt, CartItem updatedItem) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());
        Product product = productRepository.findByName(updatedItem.getProductName());
        
        // Tìm giá của sản phẩm với colorName và sizeName từ updatedItem
        Optional<Double> optionalPrice = product.getColors().stream()
            .filter(color -> color.getColorName().equals(updatedItem.getColorName()))
            .flatMap(color -> color.getSizes().stream())
            .filter(size -> size.getSizeName().equals(updatedItem.getSizeName()))
            .map(Size::getPrice)
            .findFirst();

        // Kiểm tra nếu không tìm thấy giá của sản phẩm
        if (optionalPrice.isEmpty()) {
            throw new Exception("Price not found for specified color and size");
        }
        
        double price = optionalPrice.get();  // Gán giá trị price đã tìm thấy

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            for (CartItem item : cart.getItems()) {
                if (item.getProductId().equals(updatedItem.getProductId()) &&
                    item.getColorName().equals(updatedItem.getColorName()) &&
                    item.getSizeName().equals(updatedItem.getSizeName())) {
                    item.setQuantity(updatedItem.getQuantity()); // Cập nhật số lượng
                    item.setTotalPrice(updatedItem.getQuantity() * price); // Cập nhật tổng giá
                    return cartRepository.save(cart); // Lưu lại giỏ hàng đã cập nhật
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm hoặc giỏ hàng
    }


    // Xóa sản phẩm khỏi giỏ hàng
    public Cart removeItemFromCart(String jwt, String productId, String colorName, String sizeName) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            // Xóa sản phẩm dựa trên productId, color, và sizeName
            cart.getItems().removeIf(item -> 
                item.getProductId().equals(productId) && 
                item.getColorName().equals(colorName) && // Giả sử Color có thuộc tính name
                item.getSizeName().equals(sizeName));
            return cartRepository.save(cart); // Lưu giỏ hàng sau khi xóa
        }
        return null; // Trả về null nếu không tìm thấy giỏ hàng
    }
    

    // Xóa toàn bộ giỏ hàng (khi thanh toán hoặc hủy đơn)
    public void clearCart(String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);  // Lấy thông tin người dùng từ JWT
        Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            cartRepository.save(cart); // Xóa tất cả các sản phẩm trong giỏ hàng
        }
    }
}
