package com.example.threaddit.service;

import com.example.threaddit.dto.vote.VoteRequest;
import com.example.threaddit.dto.vote.VoteResponse;
import com.example.threaddit.entity.Post;
import com.example.threaddit.entity.User;
import com.example.threaddit.entity.Vote;
import com.example.threaddit.mapper.VoteMapper;
import com.example.threaddit.repository.PostRepository;
import com.example.threaddit.repository.UserRepository;
import com.example.threaddit.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

	private final VoteRepository voteRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final VoteMapper voteMapper;

	public VoteResponse vote(VoteRequest request, String username) {

		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("User not found"));

		Post post = postRepository.findById(request.getPostId())
			.orElseThrow(() -> new RuntimeException("Post not found"));

		voteRepository.findByUserAndPost(user, post).ifPresent(oldVote -> {
			post.setScore(post.getScore() - oldVote.getValue());
			voteRepository.delete(oldVote);
		});

		Vote vote = voteMapper.toEntity(request);
		vote.setUser(user);
		vote.setPost(post);

		post.setScore(post.getScore() + request.getValue());

		postRepository.save(post);

		return voteMapper.toResponse(voteRepository.save(vote));
	}
}
