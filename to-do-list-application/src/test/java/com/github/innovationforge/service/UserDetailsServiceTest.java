package com.github.innovationforge.service;

import com.github.innovationforge.repository.UserDetailsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @Test
    public void loadUserByUsernameReturnsCorrectUser() {
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userDetailsRepository.loadUserByUsername("testUser")).thenReturn(mockUserDetails);

        UserDetails result = userDetailsService.loadUserByUsername("testUser");

        Assertions.assertEquals(mockUserDetails, result);
        verify(userDetailsRepository, times(1)).loadUserByUsername("testUser");
    }

    @Test
    public void loadUserByUsernameThrowsExceptionForUnknownUser() {
        when(userDetailsRepository.loadUserByUsername("unknownUser")).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("unknownUser"));
        verify(userDetailsRepository, times(1)).loadUserByUsername("unknownUser");
    }

    @Test
    public void addUserCallsCorrectMethod() {
        UserDetails mockUserDetails = mock(UserDetails.class);

        userDetailsService.addUser(mockUserDetails);

        verify(userDetailsRepository, times(1)).addUser(mockUserDetails);
    }
}