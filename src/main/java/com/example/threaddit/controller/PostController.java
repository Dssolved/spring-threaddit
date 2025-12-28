package com.example.threaddit.controller;

import com.example.threaddit.dto.post.PostRequest;
import com.example.threaddit.dto.post.PostResponse;
import com.example.threaddit.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping
	public List<PostResponse> getAll() {
		return postService.getAll();
	}

	@GetMapping("/{id}")
	public PostResponse getById(@PathVariable Long id) {
		return postService.getById(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public PostResponse create(@RequestBody PostRequest request, Authentication authentication) {
		return postService.create(request, authentication.getName());
	}

	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public PostResponse update(
		@PathVariable Long id,
		@RequestBody PostRequest request,
		Authentication authentication
	) {
		return postService.update(id, request, authentication.getName());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable Long id) {
		postService.delete(id);
	}
}

