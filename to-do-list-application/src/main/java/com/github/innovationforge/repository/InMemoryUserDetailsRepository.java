package com.github.innovationforge.repository;// InMemoryUserDetailsRepository.java
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.innovationforge.util.UserConverter;
import com.github.innovationforge.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.PostConstruct;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserDetailsRepository implements UserDetailsRepository {

    private List<UserDetails> users = new ArrayList<>();
    private final ObjectMapper objectMapper;
    private final UserConverter userConverter;

    @PostConstruct
    public void init() {
        loadInitialData();
    }

    private void loadInitialData() {
        log.info("Loading initial data");
        try (InputStream inputStream = getClass().getResourceAsStream("/data/initial-user-list.json")) {
            if (inputStream != null) {
                log.info("Loading initial data from {}", inputStream);
                List<User> userList = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});
                users = userConverter.convertToUserDetailsList(userList);
            }
            log.info("Loaded {} users", users.size());
        } catch (IOException e) {
            log.error("Error loading initial data", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserDetails> user = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        return user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public void addUser(UserDetails newUser) {
        users.add(newUser);
    }
}
