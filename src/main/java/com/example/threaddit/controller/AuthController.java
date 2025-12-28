package com.example.threaddit.controller;

import com.example.threaddit.dto.auth.RegisterRequest;
import com.example.threaddit.dto.user.UserResponse;
import com.example.threaddit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	public UserResponse register(@RequestBody RegisterRequest request) {
		return userService.register(request);
	}
}
