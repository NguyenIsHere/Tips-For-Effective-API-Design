package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.request.AddProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem addItemToCart(AddProductRequest addProductRequest, String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);

        Product product = productService.getProductById(addProductRequest.getProductId());

        Cart cart = cartRepository.findByUserId(user.getId());

        for(CartItem cartItem : cart.getCartItems()){
            if(cartItem.getProduct().equals(product)){
                int newQuantity = cartItem.getQuantity() + addProductRequest.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(addProductRequest.getQuantity());
        newCartItem.setCart(cart);
        newCartItem.setTotalPrice(addProductRequest.getQuantity()*product.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        cart.getCartItems().add(savedCartItem);

        return  savedCartItem;
    }

    public CartItem updateCartItemQuantity(String cartItemId, int quantity) throws Exception{
        Optional<CartItem> optionalCartItem= cartItemRepository.findById(cartItemId);
        if(optionalCartItem.isEmpty()){
            throw new Exception("cart item not found");
        }
        CartItem cartItem = optionalCartItem.get();
        cartItem.setQuantity(quantity);

        cartItem.setTotalPrice(quantity*cartItem.getProduct().getPrice());

        return cartItem;
    }

    public Cart removeItemFromCart(String cartItemId, String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartRepository.findByUserId(user.getId());
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if(optionalCartItem.isEmpty()){
            throw new Exception("cart item not found");
        }

        CartItem cartItem = optionalCartItem.get();
        cart.getCartItems().remove(cartItem);

        return cartRepository.save(cart);
    }

    public Cart findCartById(String id) throws Exception{
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if(optionalCart.isEmpty()){
            throw new Exception("cart not found with id " + id);
        }

        return optionalCart.get();
    }

    public Long calculateCartTotal(Cart cart) throws Exception {
        Long total = 0L;
        for(CartItem cartItem :cart.getCartItems()){
            total+=cartItem.getProduct().getPrice()*cartItem.getQuantity();
        }
        return total;
    }

    public Cart findCartByUserId(String userId) throws Exception{
        Cart cart = cartRepository.findByUserId(userId);
        cart.setTotal(calculateCartTotal(cart));
        return cart;
    }

    public Cart clearCart(String userId) throws Exception {
        Cart cart = findCartByUserId(userId);
        cart.getCartItems().clear();
        return cartRepository.save(cart);
    }
}
