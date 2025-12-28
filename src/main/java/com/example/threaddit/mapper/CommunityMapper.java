package com.example.threaddit.mapper;

import com.example.threaddit.dto.community.CommunityRequest;
import com.example.threaddit.dto.community.CommunityResponse;
import com.example.threaddit.entity.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Community toEntity(CommunityRequest dto);

	@Mapping(target = "createdBy", source = "createdBy.username")
	CommunityResponse toResponse(Community community);

	List<CommunityResponse> toResponseList(List<Community> communities);
}


