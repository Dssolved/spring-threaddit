package com.example.threaddit.controller;

import com.example.threaddit.dto.user.ChangeRoleRequest;
import com.example.threaddit.dto.user.UserResponse;
import com.example.threaddit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public UserResponse getProfile(Authentication auth) {
		return userService.getProfile(auth.getName());
	}

	@PutMapping("/me/password")
	public void changePassword(
		Authentication auth,
		@RequestParam String newPassword) {
		userService.changePassword(auth.getName(), newPassword);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/roles/add")
	public void addRole(
		@PathVariable Long id,
		@RequestBody ChangeRoleRequest request
	) {
		userService.addRole(id, request.getRole());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/roles/remove")
	public void removeRole(
		@PathVariable Long id,
		@RequestBody ChangeRoleRequest request
	) {
		userService.removeRole(id, request.getRole());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/block")
	public void block(@PathVariable Long id) {
		userService.blockUser(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/unblock")
	public void unBlock(@PathVariable Long id) {
		userService.unBlockUser(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		userService.deleteUser(id);
	}

}
