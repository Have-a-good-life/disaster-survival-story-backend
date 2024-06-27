package com.team1.main;

import com.team1.main.domain.db.application.DbService;
import com.team1.main.domain.db.domain.Situation;
import com.team1.main.domain.db.dto.CreateDbRequestDto;
import com.team1.main.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class Controller {

	private final BedRockService bedRockService;
	private final DbService dbService;

	@GetMapping("/health_check")
	public String healthCheck() {
		return "Server is OK";
	}

	@PostMapping("/data")
	public ApiResponse<Situation> createData(@RequestBody CreateDbRequestDto dto) {
		return dbService.createtData(dto.toService());
	}

	@GetMapping("/data")
	public ApiResponse<List<Situation>> retrieveAllData() {
		return dbService.retrieveAllData();
	}

	@GetMapping("/getRandomSituation")
	public ResponseEntity<Situation> retrieveRandomSituation() {
		return dbService.retrieveRandomSituation();
	}
}
