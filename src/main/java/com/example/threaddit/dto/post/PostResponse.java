package com.example.threaddit.dto.post;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

	private Long id;
	private String title;
	private String content;
	private String author;
	private String community;
	private int score;
	private LocalDateTime createdAt;
}

