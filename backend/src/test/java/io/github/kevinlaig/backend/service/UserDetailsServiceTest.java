package io.github.kevinlaig.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDetailsServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void shouldLoadUserByUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("kevin");
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("kevin");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
        assertThat(userDetails.getAuthorities()).extracting("authority")
                .contains("ROLE_USER");
    }
}
