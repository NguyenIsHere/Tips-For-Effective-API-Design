package com.example.demo.repository;

import com.example.demo.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);
    public Page<User> findAll(Pageable pageable);
}
