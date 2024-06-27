package com.team1.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class Controller {

	@GetMapping("/health_check")
	public String healthCheck() {
		return "Server is OK";
	}

}
