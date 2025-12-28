package com.example.threaddit.service;

import com.example.threaddit.dto.auth.RegisterRequest;
import com.example.threaddit.dto.user.UserResponse;
import com.example.threaddit.entity.Role;
import com.example.threaddit.entity.User;
import com.example.threaddit.mapper.UserMapper;
import com.example.threaddit.repository.RoleRepository;
import com.example.threaddit.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	public UserResponse register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("Username already exists");
		}

		Role userRole = roleRepository.findByName("USER")
			.orElseThrow();

		User user = userMapper.fromRegisterRequest(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(Set.of(userRole));
		user.setActive(true);

		return userMapper.toResponse(userRepository.save(user));
	}

	public User getEntityByUsername(String username) {
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));
	}

	public UserResponse getProfile(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow();

		return userMapper.toResponse(user);
	}

	public void changePassword(String username, String newPassword) {
		User user = userRepository.findByUsername(username)
			.orElseThrow();

		user.setPassword(passwordEncoder.encode(newPassword));
	}

	public boolean isAdmin(String username) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		return user.getRoles().stream()
			.anyMatch(role -> role.getName().equals("ADMIN"));
	}

	public boolean hasRole(String username, String roleName) {
		User user = getEntityByUsername(username);
		return user.getRoles().stream()
			.anyMatch(r -> r.getName().equals(roleName));
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void blockUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow();
		user.setActive(false);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void unBlockUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow();
		user.setActive(true);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		userRepository.delete(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void addRole(Long userId, String roleName) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		Role role = roleRepository.findByName(roleName)
			.orElseThrow(() -> new EntityNotFoundException("Role not found"));

		user.getRoles().add(role);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void removeRole(Long userId, String roleName) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new EntityNotFoundException("User not found"));

		user.getRoles().removeIf(role -> role.getName().equals(roleName));
	}
}


