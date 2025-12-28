package com.example.threaddit.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

	private Long id;
	private String content;
	private String author;
	private Long postId;
	private LocalDateTime createdAt;
}
