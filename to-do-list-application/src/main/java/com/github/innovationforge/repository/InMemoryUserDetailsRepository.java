package com.github.innovationforge.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.innovationforge.model.User;
import com.github.innovationforge.util.UserConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

@Repository
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserDetailsRepository implements UserDetailsRepository {

    private final AtomicReference<List<UserDetails>> userDetailsRef = new AtomicReference<>(Collections.emptyList());
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
                userDetailsRef.set(userConverter.convertToUserDetailsList(userList));
            }
            log.info("Loaded {} users", userDetailsRef.get().size());
        } catch (IOException e) {
            log.error("Error loading initial data", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserDetails> user = userDetailsRef.get().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();

        return user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public synchronized void addUser(UserDetails newUser) {
        List<UserDetails> mutableusersList = new ArrayList<>(userDetailsRef.get());
        mutableusersList.add(newUser);
        userDetailsRef.set(Collections.unmodifiableList(mutableusersList));
    }
}