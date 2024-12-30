package com.example.demo;

import com.example.demo.config.JwtProvider;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserByJwtToken() throws Exception {
        String jwt = "mock-jwt";
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(jwtProvider.getEmailFromJwt(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findUserByJwtToken(jwt);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(jwtProvider, times(1)).getEmailFromJwt(jwt);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindUserByEmail_UserExists() throws Exception {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> userService.findUserByEmail(email));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetAllUsers() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setEmail("user@example.com");
        Page<User> users = new PageImpl<>(Collections.singletonList(user));

        when(userRepository.findAll(pageable)).thenReturn(users);

        Page<User> result = userService.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("user@example.com", result.getContent().get(0).getEmail());
        verify(userRepository, times(1)).findAll(pageable);
    }
}

