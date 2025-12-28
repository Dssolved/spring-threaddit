package com.example.threaddit.controller;

import com.example.threaddit.dto.comment.CommentCreateRequest;
import com.example.threaddit.dto.comment.CommentResponse;
import com.example.threaddit.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/post/{postId}")
	public List<CommentResponse> getByPost(@PathVariable Long postId) {
		return commentService.getByPost(postId);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public CommentResponse create(@RequestBody CommentCreateRequest request, Authentication authentication) {
		return commentService.create(request, authentication.getName());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable Long id) {
		commentService.delete(id);
	}
}

