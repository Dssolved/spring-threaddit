package com.example.threaddit.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResponse {
	private Long id;
	private Long userId;
	private Long postId;
	private int value;
}
