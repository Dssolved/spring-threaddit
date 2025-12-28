package com.example.threaddit.service;

import com.example.threaddit.entity.Role;
import com.example.threaddit.entity.User;
import com.example.threaddit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	private User user;

	@BeforeEach
	void setUp() {
		Role role = Role.builder().name("USER").build();
		user = User.builder()
			.username("testuser")
			.password("password")
			.active(true)
			.roles(Set.of(role))
			.build();
	}

	@Test
	void loadUserByUsername_Success() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

		assertNotNull(userDetails);
		assertEquals("testuser", userDetails.getUsername());
		assertTrue(userDetails.getAuthorities().stream()
			.anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
	}

	@Test
	void loadUserByUsername_NotFound_ThrowsException() {
		when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("unknown"));
	}
}
