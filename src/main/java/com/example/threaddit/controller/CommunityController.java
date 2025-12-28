package com.example.threaddit.controller;

import com.example.threaddit.dto.community.CommunityRequest;
import com.example.threaddit.dto.community.CommunityResponse;
import com.example.threaddit.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

	private final CommunityService communityService;

	@GetMapping
	public List<CommunityResponse> getAll() {
		return communityService.getAll();
	}

	@GetMapping("/{id}")
	public CommunityResponse getById(@PathVariable Long id) {
		return communityService.getById(id);
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public CommunityResponse create(
		@RequestBody CommunityRequest request,
		Authentication authentication) {

		return communityService.create(request, authentication.getName());
	}

	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public CommunityResponse update(
		@PathVariable Long id,
		@RequestBody CommunityRequest request,
		Authentication authentication
	) {
		return communityService.update(id, request, authentication.getName());
	}


	@DeleteMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public void delete(
		@PathVariable Long id,
		Authentication authentication) {

		communityService.delete(id, authentication.getName());
	}
}
