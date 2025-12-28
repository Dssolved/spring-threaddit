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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

	@Mock
	private VoteRepository voteRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private VoteMapper voteMapper;

	@InjectMocks
	private VoteService voteService;

	private User user;
	private Post post;
	private Vote vote;

	@BeforeEach
	void setUp() {
		user = User.builder().id(1L).username("testuser").build();
		post = Post.builder().id(1L).title("Test Post").score(0).build();
		vote = Vote.builder().id(1L).user(user).post(post).value(1).build();
	}

	@Test
	void vote_NewVote_Success() {
		VoteRequest request = new VoteRequest(1L, 1);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		when(voteRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());
		when(voteMapper.toEntity(request)).thenReturn(vote);
		when(voteRepository.save(any(Vote.class))).thenReturn(vote);
		when(voteMapper.toResponse(any(Vote.class))).thenReturn(new VoteResponse(1L, 1L, 1L, 1));

		VoteResponse result = voteService.vote(request, "testuser");

		assertNotNull(result);
		assertEquals(1, post.getScore());
		verify(postRepository).save(post);
		verify(voteRepository).save(any(Vote.class));
	}

	@Test
	void vote_ChangeVote_Success() {
		VoteRequest request = new VoteRequest(1L, -1);
		Vote oldVote = Vote.builder().id(1L).user(user).post(post).value(1).build();
		post.setScore(1);

		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		when(voteRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(oldVote));

		Vote newVote = Vote.builder().id(2L).user(user).post(post).value(-1).build();
		when(voteMapper.toEntity(request)).thenReturn(newVote);
		when(voteRepository.save(any(Vote.class))).thenReturn(newVote);
		when(voteMapper.toResponse(any(Vote.class))).thenReturn(new VoteResponse(2L, 1L, 1L, -1));

		VoteResponse result = voteService.vote(request, "testuser");

		assertNotNull(result);
		assertEquals(-1, post.getScore());
		verify(voteRepository).delete(oldVote);
		verify(postRepository).save(post);
	}
}
