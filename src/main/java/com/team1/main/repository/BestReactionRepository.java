package com.team1.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team1.main.domain.BestReaction;

public interface BestReactionRepository extends JpaRepository<BestReaction, Integer> {
}
