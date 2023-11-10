package com.github.innovationforge.service;// UserService.java
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.github.innovationforge.model.User;
import com.github.innovationforge.repository.UserRepository;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.getAllUsers();
    }

    public void registerUser(User user) {
        log.info("Registering user {}", user);
        userRepository.addUser(user);
    }

    // Add other methods as needed
}
