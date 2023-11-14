package com.github.innovationforge.model;

import lombok.Data;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

@Data
public class User {
    private String username;
    private String password;
    private List<String> authorities;
}
