package com.team1.main.domain.db.dao;

import com.team1.main.domain.db.domain.Db;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbRepository extends JpaRepository<Db, Long> {
}
