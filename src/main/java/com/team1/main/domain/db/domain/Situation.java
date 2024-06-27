package com.team1.main.domain.db.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer situationId;

    private String situationName;

    private String situationDesc;

    @Builder
    public Situation(String situationName, String situationDesc) {
        this.situationName = situationName;
        this.situationDesc = situationDesc;
    }
}
