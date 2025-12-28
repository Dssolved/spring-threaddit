package com.example.threaddit.service;

import com.example.threaddit.dto.post.PostRequest;
import com.example.threaddit.dto.post.PostResponse;
import com.example.threaddit.entity.Community;
import com.example.threaddit.entity.Post;
import com.example.threaddit.entity.User;
import com.example.threaddit.mapper.PostMapper;
import com.example.threaddit.repository.CommunityRepository;
import com.example.threaddit.repository.PostRepository;
import com.example.threaddit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CommunityRepository communityRepository;
	@Mock
	private PostMapper postMapper;
	@Mock
	private UserService userService;

	@InjectMocks
	private PostService postService;

	private User user;
	private Community community;
	private Post post;

	@BeforeEach
	void setUp() {
		user = User.builder().id(1L).username("testuser").build();
		community = Community.builder().id(1L).name("testcomm").build();
		post = Post.builder()
			.id(1L)
			.title("Test Post")
			.content("Test Content")
			.author(user)
			.community(community)
			.build();
	}

	@Test
	void getAll_Success() {
		when(postRepository.findAll()).thenReturn(List.of(post));
		PostResponse response = PostResponse.builder().id(1L).title("Test Post").build();
		when(postMapper.toResponseList(anyList())).thenReturn(List.of(response));

		List<PostResponse> result = postService.getAll();

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	void getById_Success() {
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		PostResponse response = PostResponse.builder().id(1L).title("Test Post").build();
		when(postMapper.toResponse(post)).thenReturn(response);

		PostResponse result = postService.getById(1L);

		assertNotNull(result);
		assertEquals("Test Post", result.getTitle());
	}

	@Test
	void create_Success() {
		PostRequest request = new PostRequest("Test Post", "Test Content", 1L);
		when(postMapper.toEntity(request)).thenReturn(post);
		when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
		when(communityRepository.findById(1L)).thenReturn(Optional.of(community));
		when(postRepository.save(any(Post.class))).thenReturn(post);
		when(postMapper.toResponse(post)).thenReturn(PostResponse.builder().id(1L).title("Test Post").build());

		PostResponse result = postService.create(request, "testuser");

		assertNotNull(result);
		verify(postRepository).save(any(Post.class));
	}

	@Test
	void update_Success_AsAuthor() {
		PostRequest request = new PostRequest("New Title", "New Content", 1L);
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		when(postRepository.save(any(Post.class))).thenReturn(post);
		when(postMapper.toResponse(post)).thenReturn(PostResponse.builder().id(1L).title("New Title").build());

		PostResponse result = postService.update(1L, request, "testuser");

		assertNotNull(result);
		assertEquals("New Title", post.getTitle());
		verify(postRepository).save(post);
	}

	@Test
	void update_Denied_NotAllowed() {
		PostRequest request = new PostRequest("New Title", "New Content", 1L);
		when(postRepository.findById(1L)).thenReturn(Optional.of(post));
		when(userService.hasRole("otheruser", "MODERATOR")).thenReturn(false);
		when(userService.hasRole("otheruser", "ADMIN")).thenReturn(false);

		assertThrows(AccessDeniedException.class, () -> postService.update(1L, request, "otheruser"));
	}

	@Test
	void delete_Success() {
		postService.delete(1L);
		verify(postRepository).deleteById(1L);
	}
}
