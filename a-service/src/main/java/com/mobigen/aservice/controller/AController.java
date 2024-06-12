package com.mobigen.aservice.controller;

import com.mobigen.aservice.service.AService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class AController {
	private final AService aService;
	
	@GetMapping("/info")
	private Object getTest() {
		return aService.getTest();
	}
}
