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
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommunityRepository communityRepository;
	private final PostMapper postMapper;
	private final UserService userService;

	public List<PostResponse> getAll() {
		return postMapper.toResponseList(postRepository.findAll());
	}

	public PostResponse getById(Long id) {
		Post post = postRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Post not found"));
		return postMapper.toResponse(post);
	}

	public PostResponse create(PostRequest request, String username) {
		Post post = postMapper.toEntity(request);

		User author = userRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("User not found"));

		Community community = communityRepository.findById(request.getCommunityId())
			.orElseThrow(() -> new RuntimeException("Community not found"));

		post.setAuthor(author);
		post.setCommunity(community);

		return postMapper.toResponse(postRepository.save(post));
	}

	public PostResponse update(Long postId, PostRequest request, String username) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("Post not found"));

		boolean isAuthor = post.getAuthor().getUsername().equals(username);
		boolean isModerator = userService.hasRole(username, "MODERATOR");
		boolean isAdmin = userService.hasRole(username, "ADMIN");

		if (!isAuthor && !isModerator && !isAdmin) {
			throw new AccessDeniedException("You cannot update this post");
		}

		if (request.getTitle() != null) {
			post.setTitle(request.getTitle());
		}

		if (request.getContent() != null) {
			post.setContent(request.getContent());
		}

		return postMapper.toResponse(postRepository.save(post));
	}


	public void delete(Long id) {
		postRepository.deleteById(id);
	}
}


