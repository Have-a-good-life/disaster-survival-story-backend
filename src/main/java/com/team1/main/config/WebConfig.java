// package com.team1.main.global.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {
// 	@Override
// 	public void addCorsMappings(CorsRegistry registry) {
// 		registry.addMapping("/v1/**")
// 			// .allowedOrigins("http://localhost:3000")
// 			.allowedOrigins("http://team1-s3.s3-website.ap-south-1.amazonaws.com")
// 			.allowedMethods("GET", "POST", "PUT", "DELETE")
// 			.allowedHeaders("Content-Type")
// 			.exposedHeaders("Custom-Header")
// 			.allowCredentials(true)
// 			.maxAge(3600);
// 	}
// }