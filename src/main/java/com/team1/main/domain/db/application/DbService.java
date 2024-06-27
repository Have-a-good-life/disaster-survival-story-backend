package com.team1.main.domain.db.application;

import com.team1.main.domain.db.dao.DbRepository;
import com.team1.main.domain.db.dao.SituationRepository;
import com.team1.main.domain.db.domain.Situation;
import com.team1.main.domain.db.dto.CreateDbServiceRequestDto;
import com.team1.main.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DbService {

    private final DbRepository dbRepository;
    private final SituationRepository situationRepository;

    public ApiResponse<Situation> createtData(CreateDbServiceRequestDto dto) {
        Situation situation = dto.toEntity();
        Situation savedSituation = dbRepository.save(situation);
        return ApiResponse.ok("Data 목록을 성공적으로 조회했습니다.", savedSituation);
    }

    public ApiResponse<List<Situation>> retrieveAllData() {
        List<Situation> situationList = dbRepository.findAll();
        return ApiResponse.ok("Data 목록을 성공적으로 조회했습니다.", situationList);
    }

    public ResponseEntity<Situation> retrieveRandomSituation() {
        List<Situation> randomSituations = situationRepository.findRandomSituation();
        if(randomSituations.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(randomSituations.get(0));
    }

}
