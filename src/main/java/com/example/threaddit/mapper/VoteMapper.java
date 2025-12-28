package com.example.threaddit.mapper;

import com.example.threaddit.dto.vote.VoteRequest;
import com.example.threaddit.dto.vote.VoteResponse;
import com.example.threaddit.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "post", ignore = true)
	Vote toEntity(VoteRequest dto);

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "postId", source = "post.id")
	VoteResponse toResponse(Vote vote);
}

