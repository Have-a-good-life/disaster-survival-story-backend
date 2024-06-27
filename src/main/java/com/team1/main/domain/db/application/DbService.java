package com.team1.main.domain.db.application;

import com.team1.main.domain.db.dao.DbRepository;
import com.team1.main.domain.db.domain.Db;
import com.team1.main.domain.db.dto.CreateDbServiceRequestDto;
import com.team1.main.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DbService {

    private final DbRepository dbRepository;

    public ApiResponse<Db> createtData(CreateDbServiceRequestDto dto) {
        Db db = dto.toEntity();
        Db savedDb = dbRepository.save(db);
        return ApiResponse.ok("Data 목록을 성공적으로 조회했습니다.", savedDb);
    }

    public ApiResponse<List<Db>> retrieveAllData() {
        List<Db> dbList = dbRepository.findAll();
        return ApiResponse.ok("Data 목록을 성공적으로 조회했습니다.", dbList);
    }

}
