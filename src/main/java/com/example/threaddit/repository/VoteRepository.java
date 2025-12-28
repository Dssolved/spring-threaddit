package com.example.threaddit.repository;

import com.example.threaddit.entity.Post;
import com.example.threaddit.entity.User;
import com.example.threaddit.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	Optional<Vote> findByUserAndPost(User user, Post post);
}
