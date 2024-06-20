package com.mobigen.aservice.controller;

import com.mobigen.aservice.service.AService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class AController {
	private final AService aService;
	
	@Value("${spring.application.name}")
	private String serviceName;
	
	@GetMapping("/info")
	private Object getTest(HttpServletRequest request) {
		return aService.getTest(request);
	}
	
	@GetMapping("/error")
	private void getError(HttpServletRequest request) {
		aService.getError(request);
	}
}
