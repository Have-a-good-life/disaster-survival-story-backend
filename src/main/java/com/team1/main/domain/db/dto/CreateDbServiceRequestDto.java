package com.team1.main.domain.db.dto;

import com.team1.main.domain.db.domain.Situation;

public record CreateDbServiceRequestDto(
        String situationName,
        String situationDesc
) {
    public CreateDbServiceRequestDto(String situationName, String situationDesc) {
        this.situationName = situationName;
        this.situationDesc = situationDesc;
    }
    public Situation toEntity() {
        return Situation.builder()
                .situationName(situationName)
                .situationDesc(situationDesc)
                .build();
    }
}
