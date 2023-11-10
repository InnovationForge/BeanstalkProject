package com.github.innovationforge.repository;

// UserRepository.java
import java.util.List;

import com.github.innovationforge.model.User;

public interface UserRepository {
    List<User> getAllUsers();

    void addUser(User user);

    // Add other methods as needed
}
