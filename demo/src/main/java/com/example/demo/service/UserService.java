package com.example.demo.service;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtProvider;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    public User findUserByJwtToken(String jwt) throws Exception{
        String email = jwtProvider.getEmailFromJwt(jwt);
        User user = findUserByEmail(email);
        return user;
    }

    public User findUserByEmail(String email) throws Exception{
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new Exception("User not found");
        }
        return user;
    }
}
