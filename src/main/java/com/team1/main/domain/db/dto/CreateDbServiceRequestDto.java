package com.team1.main.domain.db.dto;

import com.team1.main.domain.db.domain.Db;

public record CreateDbServiceRequestDto(
        String data,
        String description
) {
    public CreateDbServiceRequestDto(String data, String description) {
        this.data = data;
        this.description = description;
    }
    public Db toEntity() {
        return Db.builder()
                .data(data)
                .description(description)
                .build();
    }
}
