package com.team1.main;

import java.util.List;
import java.util.Random;

import com.team1.main.domain.Situation;
import com.team1.main.repository.SituationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class Controller {

	private final SituationRepository situationRepository;

	@GetMapping("/health_check")
	public String healthCheck() {
		return "Server is OK";
	}

	@GetMapping("/get_random_situation")
	public ResponseEntity<Situation> getRandomSituation() {
		List<Situation> situations = situationRepository.findAll();
		if (situations.isEmpty())
			return ResponseEntity.noContent().build();
		int randomIndex = new Random().nextInt(situations.size());
		Situation situation = situations.get(randomIndex);
		return ResponseEntity.ok(situation);
	}
}
