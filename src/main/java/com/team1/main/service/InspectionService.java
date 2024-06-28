package com.team1.main.service;

import org.springframework.stereotype.Service;

@Service
public class InspectionService {

	public String inspectUserReaction(String userReaction) {
		if (userReaction.length() < 10)
			return "응답 내용이 너무 짧습니다. 재난은 신중한 대응을 통해 위험으로부터 벗어날 수 있습니다.";
		return null;
	}

	public boolean inspectEvaluation(String evaluation) {
		if (evaluation.length() < 200) {
			return false;
		}
		return true;
	}

	public boolean inspectInjury(String evaluation) {
		return (evaluation.equals("생존") || evaluation.equals("사망"));
	}

}
