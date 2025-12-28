package com.example.threaddit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
	name = "votes",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "post_id"})
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(nullable = false)
	private int value;
}

