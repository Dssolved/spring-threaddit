package com.example.threaddit.dto.community;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityResponse {

	private Long id;
	private String name;
	private String description;
	private String createdBy;
}

