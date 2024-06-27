package com.team1.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team1.main.domain.ReactionMap;

public interface ReactionMapRepository extends JpaRepository<ReactionMap, Integer> {
	List<ReactionMap> findBySituationId(Integer situationId);
}
