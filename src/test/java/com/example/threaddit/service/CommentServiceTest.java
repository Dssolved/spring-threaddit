package com.example.threaddit.service;

import com.example.threaddit.dto.comment.CommentCreateRequest;
import com.example.threaddit.dto.comment.CommentResponse;
import com.example.threaddit.entity.Comment;
import com.example.threaddit.entity.Post;
import com.example.threaddit.entity.User;
import com.example.threaddit.mapper.CommentMapper;
import com.example.threaddit.repository.CommentRepository;
import com.example.threaddit.repository.PostRepository;
import com.example.threaddit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@Mock
	private CommentRepository commentRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PostRepository postRepository;
	@Mock
	private CommentMapper commentMapper;

	@InjectMocks
	private CommentService commentService;

	private User user;
	private Post post;
	private Comment comment;

	@BeforeEach
	void setUp() {
		user = User.builder().id(1L).username("testuser").build();
		post = Post.builder().id(1L).title("Test Post").build();
		comment = Comment.builder().id(1L).content("Test Comment").author(user).post(post).build();
	}

	@Test
	void getByPost_Success() {
		when(commentRepository.findByPostId(1L)).thenReturn(List.of(comment));
		when(commentMapper.toResponseList(anyList())).thenReturn(List.of(new CommentResponse(1L, "Test Comment", "testuser", 1L, null)));

		List<CommentResponse> result = commentService.getByPost(1L);

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	void create_Success() {
		CommentCreateRequest request = new CommentCreateRequest("Test Comment", 1L);
		when(commentMapper.toEntity(request)).thenReturn(comment);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);
		when(commentMapper.toResponse(comment)).thenReturn(new CommentResponse(1L, "Test Comment", "testuser", 1L, null));

		CommentResponse result = commentService.create(request, "testuser");

		assertNotNull(result);
		verify(commentRepository).save(any(Comment.class));
	}

	@Test
	void delete_Success() {
		commentService.delete(1L);
		verify(commentRepository).deleteById(1L);
	}
}
