package com.github.innovationforge.util;

import com.github.innovationforge.model.User;
import com.github.innovationforge.model.UserDetailsImpl;
import com.github.innovationforge.util.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserConverterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserConverter userConverter;

    @Test
    public void convertToUserDetailsList_returnsCorrectUserDetails() {
        User user1 = new User("user1", "password1", Arrays.asList("ROLE_USER"));
        User user2 = new User("user2", "password2", Arrays.asList("ROLE_ADMIN"));
        List<User> users = Arrays.asList(user1, user2);

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        List<UserDetails> userDetails = userConverter.convertToUserDetailsList(users);

        assertEquals(2, userDetails.size());
        assertEquals(UserDetailsImpl.class, userDetails.get(0).getClass());
        assertEquals(UserDetailsImpl.class, userDetails.get(1).getClass());
    }

    @Test
    public void convertToUserDetailsList_returnsEmptyListWhenInputIsEmpty() {
        List<User> users = Arrays.asList();

        List<UserDetails> userDetails = userConverter.convertToUserDetailsList(users);

        assertEquals(0, userDetails.size());
    }

//    @Test
//    public void convertToUserDetails_encodesPasswordCorrectly() {
//        User user = new User("user", "password", Arrays.asList("ROLE_USER"));
//
//        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
//
//        UserDetails userDetails = userConverter.convertToUserDetails(user);
//
//        assertEquals("encodedPassword", userDetails.getPassword());
//    }
}