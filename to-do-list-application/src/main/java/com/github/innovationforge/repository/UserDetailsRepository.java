package com.github.innovationforge.repository;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsRepository {

    UserDetails loadUserByUsername(String username);
    void addUser(UserDetails newUser);
}
