package com.example.threaddit.mapper;

import com.example.threaddit.dto.post.PostRequest;
import com.example.threaddit.dto.post.PostResponse;
import com.example.threaddit.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "community", ignore = true)
	@Mapping(target = "score", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Post toEntity(PostRequest dto);

	@Mapping(target = "author", source = "author.username")
	@Mapping(target = "community", source = "community.name")
	PostResponse toResponse(Post post);

	List<PostResponse> toResponseList(List<Post> posts);
}
