package com.team1.main.domain.db.dao;

import com.team1.main.domain.db.domain.Situation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SituationRepository extends JpaRepository<Situation, Long> {

    @Query(value = "SELECT s FROM Situation s ORDER BY RAND()")
    List<Situation> findRandomSituation();
}
