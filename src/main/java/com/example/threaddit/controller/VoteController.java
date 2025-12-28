package com.example.threaddit.controller;

import com.example.threaddit.dto.vote.VoteRequest;
import com.example.threaddit.dto.vote.VoteResponse;
import com.example.threaddit.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

	private final VoteService voteService;

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public VoteResponse vote(@RequestBody VoteRequest request, Authentication authentication) {
		return voteService.vote(request, authentication.getName());
	}
}
