package com.team1.main.domain.db.dto;

public record CreateDbRequestDto (
    String data,
    String description
){
    public CreateDbServiceRequestDto toService() {
        return new CreateDbServiceRequestDto(data, description);
    }
}
