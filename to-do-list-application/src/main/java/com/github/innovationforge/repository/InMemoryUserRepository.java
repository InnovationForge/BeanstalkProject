package com.github.innovationforge.repository;// InMemoryUserRepository.java
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

import com.github.innovationforge.model.User;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private static final List<User> users = new ArrayList<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    // Implement other methods if needed
}
