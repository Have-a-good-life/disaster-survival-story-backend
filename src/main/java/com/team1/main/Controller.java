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
import com.team1.main.response.AWSLambdaRequest;
import com.team1.main.response.EvaluateUserReactionRequest;
import com.team1.main.response.EvaluateUserReactionResponse;

import lombok.RequiredArgsConstructor;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

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
		System.out.println(request.situation_id);
		System.out.println(request.user_reaction);
		List<ReactionMap> reactionMaps = reactionMapRepository.findBySituationId(request.situation_id);
		Situation situation = situationRepository.findById(request.situation_id).orElse(null);
		if (situation == null)
			return ResponseEntity.noContent().build();
		AWSLambdaRequest awsLambdaRequest = new AWSLambdaRequest();
		awsLambdaRequest.situation = situation.situationDesc;
		awsLambdaRequest.userReaction = request.user_reaction;
		awsLambdaRequest.bestReactions = new ArrayList<>();
		for (ReactionMap reactionMap : reactionMaps) {
			BestReaction bestReaction = bestReactionRepository.findById(reactionMap.bestReactionId).orElse(null);
			if (bestReaction == null)
				return ResponseEntity.noContent().build();
			awsLambdaRequest.bestReactions.add(bestReaction.bestReactionDesc);
		}
		String requestBody = objectMapper.writeValueAsString(awsLambdaRequest);
		System.out.println(requestBody);
		InvokeRequest invokeRequest = new InvokeRequest()
			.withFunctionName("arn:aws:lambda:ap-south-1:730335373015:function:mju-team1-question")
			.withPayload(requestBody);
		InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
		String response = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
		EvaluateUserReactionResponse evaluateUserReactionResponse = new EvaluateUserReactionResponse();
		System.out.println(response);
		
		String[] responseSplit = response.replace("\"", "").replace("\\n", "").split("/");
		evaluateUserReactionResponse.setEvaluation(responseSplit[0]);
		evaluateUserReactionResponse.setInjury(responseSplit[1]);
		
		// try {
		
		// 	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@2");
		// 	System.out.println(response);
		// 	System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@2");
		
	 //       JSONParser jsonParser = new JSONParser();
	 //       Object obj = jsonParser.parse(response);
	 //       JSONObject jsonObj = (JSONObject) obj;
			
		// 	evaluateUserReactionResponse.setEvaluation((String)jsonObj.get("evaluation"));
		// 	evaluateUserReactionResponse.setInjury((String)jsonObj.get("injury"));
		// } catch(Exception e) {
		// 	// e.printStackTrace();
		// }
		// response = response.replace("\\", "");
		// evaluateUserReactionResponse.response = response;
		return ResponseEntity.ok(evaluateUserReactionResponse);
		// return ResponseEntity.ok(jsonObj);
	}
}
