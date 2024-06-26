package com.team1.temp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HomeController{
	@GetMapping(value = "/index")
	public String test() {
		return "Hello World!";
	}
}
