package com.team1.main.domain.db.dto;

public record CreateDbRequestDto (
    String situationName,
    String situationDesc
){
    public CreateDbServiceRequestDto toService() {
        return new CreateDbServiceRequestDto(situationName, situationDesc);
    }
}
