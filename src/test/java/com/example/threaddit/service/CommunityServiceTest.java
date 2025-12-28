package com.example.threaddit.service;

import com.example.threaddit.dto.community.CommunityRequest;
import com.example.threaddit.dto.community.CommunityResponse;
import com.example.threaddit.entity.Community;
import com.example.threaddit.entity.User;
import com.example.threaddit.mapper.CommunityMapper;
import com.example.threaddit.repository.CommunityRepository;
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
class CommunityServiceTest {

	@Mock
	private CommunityRepository communityRepository;
	@Mock
	private CommunityMapper communityMapper;
	@Mock
	private UserService userService;

	@InjectMocks
	private CommunityService communityService;

	private User user;
	private Community community;

	@BeforeEach
	void setUp() {
		user = User.builder().id(1L).username("testuser").build();
		community = Community.builder().id(1L).name("testcomm").createdBy(user).build();
	}

	@Test
	void getAll_Success() {
		when(communityRepository.findAll()).thenReturn(List.of(community));
		when(communityMapper.toResponseList(anyList())).thenReturn(List.of(new CommunityResponse(1L, "testcomm", "desc", "testuser")));

		List<CommunityResponse> result = communityService.getAll();

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
	}

	@Test
	void getById_Success() {
		when(communityRepository.findById(1L)).thenReturn(Optional.of(community));
		when(communityMapper.toResponse(community)).thenReturn(new CommunityResponse(1L, "testcomm", "desc", "testuser"));

		CommunityResponse result = communityService.getById(1L);

		assertNotNull(result);
		assertEquals("testcomm", result.getName());
	}

	@Test
	void create_Success() {
		CommunityRequest request = new CommunityRequest("testcomm", "desc");
		when(userService.getEntityByUsername("testuser")).thenReturn(user);
		when(communityMapper.toEntity(request)).thenReturn(community);
		when(communityRepository.save(any(Community.class))).thenReturn(community);
		when(communityMapper.toResponse(community)).thenReturn(new CommunityResponse(1L, "testcomm", "desc", "testuser"));

		CommunityResponse result = communityService.create(request, "testuser");

		assertNotNull(result);
		verify(communityRepository).save(any(Community.class));
	}

	@Test
	void update_Success_AsOwner() {
		CommunityRequest request = new CommunityRequest("New Name", "New Desc");
		when(communityRepository.findById(1L)).thenReturn(Optional.of(community));
		when(communityRepository.save(any(Community.class))).thenReturn(community);
		when(communityMapper.toResponse(community)).thenReturn(new CommunityResponse(1L, "New Name", "New Desc", "testuser"));

		CommunityResponse result = communityService.update(1L, request, "testuser");

		assertNotNull(result);
		assertEquals("New Name", community.getName());
	}

	@Test
	void delete_Success_AsAdmin() {
		when(communityRepository.findById(1L)).thenReturn(Optional.of(community));
		when(userService.isAdmin("adminuser")).thenReturn(true);

		communityService.delete(1L, "adminuser");

		verify(communityRepository).delete(community);
	}

	@Test
	void delete_Denied_NotOwnerOrAdmin() {
		when(communityRepository.findById(1L)).thenReturn(Optional.of(community));
		when(userService.isAdmin("otheruser")).thenReturn(false);

		assertThrows(AccessDeniedException.class, () -> communityService.delete(1L, "otheruser"));
	}
}
