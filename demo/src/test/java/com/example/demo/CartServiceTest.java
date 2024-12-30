package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCartByJwtToken() throws Exception {
        String jwt = "testToken";
        User user = new User();
        user.setId("userId");

        Cart cart = new Cart();
        cart.setUserId(user.getId());

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByJwtToken(jwt);

        assertNotNull(result);
        assertEquals("userId", result.getUserId());
        verify(cartRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void testAddItemToCart_NewItem() throws Exception {
        String jwt = "testToken";
        User user = new User();
        user.setId("userId");

        Product product = new Product();
        product.setName("TestProduct");

        CartItem newItem = new CartItem();
        newItem.setProductId("productId");
        newItem.setColorName("Red");
        newItem.setSizeName("L");
        newItem.setQuantity(2);

        Size size = new Size();
        size.setSizeName("L");
        size.setPrice(50.0);

        Color color = new Color();
        color.setColorName("Red");
        color.setSizes(new ArrayList<>() {{
            add(size);
        }});

        product.setColors(new ArrayList<>() {{
            add(color);
        }});

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setItems(new ArrayList<>());

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(productRepository.findByName(newItem.getProductName())).thenReturn(product);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addItemToCart(jwt, newItem);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(100.0, result.getItems().get(0).getTotalPrice());
        assertEquals(100.0, result.getTotalPrice());
    }

    @Test
    void testUpdateCartItem() throws Exception {
        String jwt = "testToken";
        User user = new User();
        user.setId("userId");

        Product product = new Product();
        product.setName("TestProduct");

        CartItem existingItem = new CartItem();
        existingItem.setProductId("productId");
        existingItem.setColorName("Red");
        existingItem.setSizeName("L");
        existingItem.setQuantity(1);

        Size size = new Size();
        size.setSizeName("L");
        size.setPrice(50.0);

        Color color = new Color();
        color.setColorName("Red");
        color.setSizes(new ArrayList<>() {{
            add(size);
        }});

        product.setColors(new ArrayList<>() {{
            add(color);
        }});

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setItems(new ArrayList<>() {{
            add(existingItem);
        }});

        CartItem updatedItem = new CartItem();
        updatedItem.setProductId("productId");
        updatedItem.setColorName("Red");
        updatedItem.setSizeName("L");
        updatedItem.setQuantity(3);

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(productRepository.findByName(updatedItem.getProductName())).thenReturn(product);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.updateCartItem(jwt, updatedItem);

        assertNotNull(result);
        assertEquals(150.0, result.getItems().get(0).getTotalPrice());
    }

    @Test
    void testRemoveItemFromCart() throws Exception {
        String jwt = "testToken";
        User user = new User();
        user.setId("userId");

        CartItem existingItem = new CartItem();
        existingItem.setProductId("productId");
        existingItem.setColorName("Red");
        existingItem.setSizeName("L");

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setItems(new ArrayList<>() {{
            add(existingItem);
        }});

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.removeItemFromCart(jwt, "productId", "Red", "L");

        assertNotNull(result);
        assertEquals(0, result.getItems().size());
    }

    @Test
    void testClearCart() throws Exception {
        String jwt = "testToken";
        User user = new User();
        user.setId("userId");

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        cart.setItems(new ArrayList<>() {{
            add(new CartItem());
        }});

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        cartService.clearCart(jwt);

        assertEquals(0, cart.getItems().size());
        verify(cartRepository, times(1)).save(cart);
    }
}
