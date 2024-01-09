package com.github.innovationforge.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.innovationforge.util.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InMemoryUserDetailsRepositoryTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private InMemoryUserDetailsRepository inMemoryUserDetailsRepository;
    @Mock
    private UserDetails userDetails1;
    @Mock
    private UserDetails userDetails2;

    @BeforeEach
    public void setup() {
        inMemoryUserDetailsRepository.addUser(userDetails1);
        inMemoryUserDetailsRepository.addUser(userDetails2);
    }

    @Test
    public void loadUserByUsernameReturnsCorrectUser() {
        when(userDetails1.getUsername()).thenReturn("user1");

        UserDetails result = inMemoryUserDetailsRepository.loadUserByUsername("user1");

        assertEquals(userDetails1, result);
    }

    @Test
    public void loadUserByUsernameThrowsExceptionForUnknownUser() {
        when(userDetails1.getUsername()).thenReturn("user1");
        when(userDetails2.getUsername()).thenReturn("user2");

        assertThrows(UsernameNotFoundException.class, () -> inMemoryUserDetailsRepository.loadUserByUsername("unknownUser"));
    }
}