package com.example.threaddit.mapper;

import com.example.threaddit.dto.auth.RegisterRequest;
import com.example.threaddit.dto.user.UserResponse;
import com.example.threaddit.entity.Role;
import com.example.threaddit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "active", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	User fromRegisterRequest(RegisterRequest request);

	@Mapping(target = "roles", expression = "java(mapRoles(user))")
	UserResponse toResponse(User user);

	default Set<String> mapRoles(User user) {
		return user.getRoles()
			.stream()
			.map(Role::getName)
			.collect(Collectors.toSet());
	}
}
