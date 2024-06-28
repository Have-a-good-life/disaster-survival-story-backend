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
import com.team1.main.domain.BestReaction;
import com.team1.main.domain.ReactionMap;
import com.team1.main.domain.Situation;
import com.team1.main.repository.BestReactionRepository;
import com.team1.main.repository.ReactionMapRepository;
import com.team1.main.repository.SituationRepository;
import com.team1.main.response.AWSLambdaProposeRequest;
import com.team1.main.response.AWSLambdaQuestionRequest;
import com.team1.main.response.EvaluateUserReactionRequest;
import com.team1.main.response.EvaluateUserReactionResponse;
import com.team1.main.service.InspectionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {

	private final SituationRepository situationRepository;
	private final ReactionMapRepository reactionMapRepository;
	private final BestReactionRepository bestReactionRepository;

	private final InspectionService inspectionService;

	private final AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
		.withRegion(Regions.AP_SOUTH_1)
		.build();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/health_check")
	public String healthCheck() {
		return "Server is OK";
	}

	@GetMapping("/get_random_situation")
	@ResponseBody
	public ResponseEntity<Situation> getRandomSituation() {
		List<Situation> situations = situationRepository.findAll();
		if (situations.isEmpty())
			return ResponseEntity.noContent().build();
		int randomIndex = new Random().nextInt(situations.size());
		Situation situation = situations.get(randomIndex);
		return ResponseEntity.ok(situation);
	}

	@PostMapping("/evaluate_user_reaction")
	@ResponseBody
	public ResponseEntity<EvaluateUserReactionResponse> evaluateUserReaction(
		@RequestBody EvaluateUserReactionRequest request) throws JsonProcessingException {
		List<ReactionMap> reactionMaps = reactionMapRepository.findBySituationId(request.situation_id);
		Situation situation = situationRepository.findById(request.situation_id).orElse(null);
		if (situation == null)
			return ResponseEntity.noContent().build();
		AWSLambdaQuestionRequest awsLambdaQuestionRequest = new AWSLambdaQuestionRequest();
		awsLambdaQuestionRequest.situation = situation.situationDesc;
		awsLambdaQuestionRequest.userReaction = request.user_reaction;
		awsLambdaQuestionRequest.bestReactions = new ArrayList<>();
		EvaluateUserReactionResponse evaluateUserReactionResponse = new EvaluateUserReactionResponse();
		String inspectionResult = null;
		if ((inspectionResult = inspectionService.inspectUserReaction(request.user_reaction)) != null) {
			evaluateUserReactionResponse.setEvaluation(inspectionResult);
			evaluateUserReactionResponse.setInjury("사망");
			return ResponseEntity.ok(evaluateUserReactionResponse);
		}
		for (ReactionMap reactionMap : reactionMaps) {
			BestReaction bestReaction = bestReactionRepository.findById(reactionMap.bestReactionId).orElse(null);
			if (bestReaction == null)
				return ResponseEntity.noContent().build();
			awsLambdaQuestionRequest.bestReactions.add(bestReaction.bestReactionDesc);
		}

		String evaluation = null;
		String injury = null;
		String requestBody = objectMapper.writeValueAsString(awsLambdaQuestionRequest);

		do {
			InvokeRequest invokeRequest = new InvokeRequest()
				.withFunctionName("arn:aws:lambda:ap-south-1:730335373015:function:mju-team1-question")
				.withPayload(requestBody);
			InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
			String response = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
			System.out.println(response);
			String[] responseSplit = response.replace("\"", "").replace("\\n", "").split("/");
			evaluation = responseSplit[0];
			injury = responseSplit[1];
		} while (!inspectionService.inspectEvaluation(evaluation) || !inspectionService.inspectInjury(injury));

		evaluateUserReactionResponse.setEvaluation(evaluation);
		evaluateUserReactionResponse.setInjury(injury);

		return ResponseEntity.ok(evaluateUserReactionResponse);
	}

	@GetMapping("/get_best_reactions/{situationId}")
	public String getBestReactions(@PathVariable int situationId) throws JsonProcessingException {
		List<ReactionMap> reactionMaps = reactionMapRepository.findBySituationId(situationId);
		AWSLambdaProposeRequest awsLambdaProposeRequest = new AWSLambdaProposeRequest();
		awsLambdaProposeRequest.bestReactions = new ArrayList<>();
		for (ReactionMap reactionMap : reactionMaps) {
			BestReaction bestReaction = bestReactionRepository.findById(reactionMap.bestReactionId).orElse(null);
			if (bestReaction == null)
				return null;
			awsLambdaProposeRequest.bestReactions.add(bestReaction.bestReactionDesc);
		}
		String requestBody = objectMapper.writeValueAsString(awsLambdaProposeRequest);
		InvokeRequest invokeRequest = new InvokeRequest()
			.withFunctionName("arn:aws:lambda:ap-south-1:730335373015:function:mju-team1-propose")
			.withPayload(requestBody);
		InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
		return new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
	}
}
