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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final CommentMapper commentMapper;

	public List<CommentResponse> getByPost(Long postId) {
		return commentMapper.toResponseList(
			commentRepository.findByPostId(postId)
		);
	}

	public CommentResponse create(CommentCreateRequest request, String username) {
		Comment comment = commentMapper.toEntity(request);

		User author = userRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("User not found"));

		Post post = postRepository.findById(request.getPostId())
			.orElseThrow(() -> new RuntimeException("Post not found"));

		comment.setAuthor(author);
		comment.setPost(post);

		return commentMapper.toResponse(commentRepository.save(comment));
	}

	public void delete(Long id) {
		commentRepository.deleteById(id);
	}
}


