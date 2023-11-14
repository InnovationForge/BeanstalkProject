package com.github.innovationforge.util;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import com.github.innovationforge.model.User;
import com.github.innovationforge.model.UserDetailsImpl;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public List<UserDetails> convertToUserDetailsList(List<User> userList) {
        return userList.stream()
                .map(this::convertToUserDetails)
                .collect(Collectors.toList());
    }

    private UserDetails convertToUserDetails(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                authorities
        );
    }
}
