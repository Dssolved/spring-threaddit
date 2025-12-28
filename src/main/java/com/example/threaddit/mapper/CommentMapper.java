package com.example.threaddit.mapper;

import com.example.threaddit.dto.comment.CommentCreateRequest;
import com.example.threaddit.dto.comment.CommentResponse;
import com.example.threaddit.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "post", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Comment toEntity(CommentCreateRequest dto);

	@Mapping(target = "author", source = "author.username")
	@Mapping(target = "postId", source = "post.id")
	CommentResponse toResponse(Comment comment);

	List<CommentResponse> toResponseList(List<Comment> comments);
}

