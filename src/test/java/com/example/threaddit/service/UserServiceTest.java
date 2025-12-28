package com.example.threaddit.service;

import com.example.threaddit.dto.auth.RegisterRequest;
import com.example.threaddit.dto.user.UserResponse;
import com.example.threaddit.entity.Role;
import com.example.threaddit.entity.User;
import com.example.threaddit.mapper.UserMapper;
import com.example.threaddit.repository.RoleRepository;
import com.example.threaddit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private UserService userService;

	private User user;
	private Role role;

	@BeforeEach
	void setUp() {
		role = Role.builder().id(1L).name("USER").build();
		user = User.builder()
			.id(1L)
			.username("testuser")
			.password("password")
			.active(true)
			.roles(new HashSet<>(Set.of(role)))
			.build();
	}

	@Test
	void register_Success() {
		RegisterRequest request = new RegisterRequest("testuser", "password");
		when(userRepository.existsByUsername("testuser")).thenReturn(false);
		when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
		when(userMapper.fromRegisterRequest(request)).thenReturn(user);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(userMapper.toResponse(user)).thenReturn(new UserResponse(1L, "testuser", true, Set.of("USER")));

		UserResponse response = userService.register(request);

		assertNotNull(response);
		assertEquals("testuser", response.getUsername());
		verify(userRepository).save(any(User.class));
	}

	@Test
	void register_UsernameExists_ThrowsException() {
		RegisterRequest request = new RegisterRequest("testuser", "password");
		when(userRepository.existsByUsername("testuser")).thenReturn(true);

		assertThrows(IllegalArgumentException.class, () -> userService.register(request));
	}

	@Test
	void getProfile_Success() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(userMapper.toResponse(user)).thenReturn(new UserResponse(1L, "testuser", true, Set.of("USER")));

		UserResponse response = userService.getProfile("testuser");

		assertNotNull(response);
		assertEquals("testuser", response.getUsername());
	}

	@Test
	void changePassword_Success() {
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

		userService.changePassword("testuser", "newPassword");

		assertEquals("encodedNewPassword", user.getPassword());
	}

	@Test
	void isAdmin_True() {
		Role adminRole = Role.builder().name("ADMIN").build();
		user.getRoles().add(adminRole);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

		boolean result = userService.isAdmin("testuser");

		assertTrue(result);
	}

	@Test
	void blockUser_Success() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userService.blockUser(1L);

		assertFalse(user.isActive());
	}
}
