package com.example.threaddit.dto.user;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

	private Long id;
	private String username;
	private boolean active;
	private Set<String> roles;
}

