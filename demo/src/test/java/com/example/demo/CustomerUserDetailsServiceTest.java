package com.example.demo;

import com.example.demo.model.USER_ROLE;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CustomerUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerUserDetailsService customerUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role.toString())));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customerUserDetailsService.loadUserByUsername(email);
        });

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
    }
}
