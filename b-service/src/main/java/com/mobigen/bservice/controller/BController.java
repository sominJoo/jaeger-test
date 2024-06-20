package com.mobigen.bservice.controller;

import com.mobigen.bservice.service.BService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class BController {
	private final BService bService;
	
	@GetMapping("/info")
	private Object getTest() {
		return bService.getTest();
	}
}
