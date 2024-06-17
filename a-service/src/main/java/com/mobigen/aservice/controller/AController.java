package com.mobigen.aservice.controller;

import com.mobigen.aservice.service.AService;
import io.opentracing.Span;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.opentracing.Tracer;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class AController {
	private final AService aService;
	private static final Logger logger = LoggerFactory.getLogger(AController.class);
	private final Tracer tracer;
	
	@GetMapping("/info")
	private Object getTest() {
		Span parentSpan = tracer.scopeManager().activeSpan();
		Span spanPhase1 = tracer.buildSpan("spanPhase_1").asChildOf(parentSpan).start();
		try {
			spanPhase1.log("User Micro Service Call. 서비스 로그 확인");  // 로그1
		} finally {
			spanPhase1.finish();
		}

		logger.info("getTest AController");
		return aService.getTest();
	}
	
	@GetMapping("/error")
	private Object getError() {
		Span parentSpan = tracer.scopeManager().activeSpan();
		Span spanPhase1 = tracer.buildSpan("spanPhase_error").asChildOf(parentSpan).start();
		String msg = "에러 발생";
		
		try {
			spanPhase1.log(msg);
			throw new RuntimeException(msg);
		} finally {
			spanPhase1.finish();
		}
	}
}
