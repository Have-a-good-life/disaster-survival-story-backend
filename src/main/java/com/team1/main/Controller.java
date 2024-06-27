package com.team1.main;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.main.domain.ReactionMap;
import com.team1.main.domain.Situation;
import com.team1.main.repository.ReactionMapRepository;
import com.team1.main.repository.SituationRepository;
import com.team1.main.response.AWSLambdaRequest;
import com.team1.main.response.EvaluateUserReactionRequest;
import com.team1.main.response.EvaluateUserReactionResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class Controller {

	private final SituationRepository situationRepository;
	private final ReactionMapRepository reactionMapRepository;

	private final AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
		.withRegion(Regions.US_EAST_1)
		.build();
	private final ObjectMapper objectMapper = new ObjectMapper();

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

	@PostMapping("/evaluate_user_reaction")
	public ResponseEntity<EvaluateUserReactionResponse> evaluateUserReaction(
		@RequestBody EvaluateUserReactionRequest request) throws JsonProcessingException {
		List<ReactionMap> reactionMaps = reactionMapRepository.findBySituationId(request.situationId);
		Situation situation = situationRepository.findById(request.situationId).orElse(null);
		if (situation == null)
			return ResponseEntity.noContent().build();
		AWSLambdaRequest awsLambdaRequest = new AWSLambdaRequest();
		awsLambdaRequest.situation = situation.situationDesc;
		awsLambdaRequest.userReaction = request.userReaction;
		awsLambdaRequest.bestReactions = new ArrayList<>();
		for (ReactionMap reactionMap : reactionMaps)
			awsLambdaRequest.bestReactions.add("OK");
		String requestBody = objectMapper.writeValueAsString(request);
		InvokeRequest invokeRequest = new InvokeRequest()
			.withFunctionName("arn:aws:lambda:ap-south-1:730335373015:function:mju-team1-question")
			.withPayload(requestBody);
		InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
		String response = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
		EvaluateUserReactionResponse evaluateUserReactionResponse = new EvaluateUserReactionResponse();
		evaluateUserReactionResponse.response = response;
		return ResponseEntity.ok(evaluateUserReactionResponse);
	}
}
