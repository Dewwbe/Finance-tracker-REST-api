package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<com.example.demo.Model.User, String> {
    Optional<com.example.demo.Model.User> findByEmail(String email);
}
