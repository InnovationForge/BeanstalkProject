package com.github.innovationforge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private List<String> authorities;
}
