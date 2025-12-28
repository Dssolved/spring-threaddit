package com.example.threaddit.service;

import com.example.threaddit.dto.community.CommunityRequest;
import com.example.threaddit.dto.community.CommunityResponse;
import com.example.threaddit.entity.Community;
import com.example.threaddit.entity.User;
import com.example.threaddit.mapper.CommunityMapper;
import com.example.threaddit.repository.CommunityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

	private final CommunityRepository communityRepository;
	private final CommunityMapper communityMapper;
	private final UserService userService;

	@Transactional
	public List<CommunityResponse> getAll() {
		return communityMapper.toResponseList(communityRepository.findAll());
	}

	@Transactional
	public CommunityResponse getById(Long id) {
		Community community = communityRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Community not found"));
		return communityMapper.toResponse(community);
	}


	public CommunityResponse create(CommunityRequest request, String username) {
		User creator = userService.getEntityByUsername(username);

		Community community = communityMapper.toEntity(request);
		community.setCreatedBy(creator);

		return communityMapper.toResponse(
			communityRepository.save(community)
		);
	}

	public CommunityResponse update(Long id, CommunityRequest request, String username) {
		Community community = communityRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Community not found"));

		boolean isOwner = community.getCreatedBy().getUsername().equals(username);
		boolean isModerator = userService.hasRole(username, "MODERATOR");
		boolean isAdmin = userService.hasRole(username, "ADMIN");

		if (!isOwner && !isModerator && !isAdmin) {
			throw new AccessDeniedException("You cannot update this community");
		}

		if (request.getName() != null) {
			community.setName(request.getName());
		}

		if (request.getDescription() != null) {
			community.setDescription(request.getDescription());
		}

		return communityMapper.toResponse(communityRepository.save(community));
	}


	public void delete(Long id, String username) {
		Community community = communityRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Community not found"));

		boolean isOwner = community.getCreatedBy().getUsername().equals(username);
		boolean isAdmin = userService.isAdmin(username);

		if (!isOwner && !isAdmin) {
			throw new AccessDeniedException("You cannot delete this community");
		}

		communityRepository.delete(community);
	}
}

