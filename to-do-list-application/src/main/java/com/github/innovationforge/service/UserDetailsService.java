package com.github.innovationforge.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final com.github.innovationforge.repository.UserDetailsRepository userDetailsRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws org.springframework.security.core.userdetails.UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        return userDetailsRepository.loadUserByUsername(username);
    }
}
