package com.mobigen.aservice.service;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AService {
	private static final Logger logger = LoggerFactory.getLogger(AService.class);
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	private Tracer tracer;
	public Object getTest() {
		logger.info("Incoming request at {} for request /path1 ", applicationName);
		logger.info("A service getTest 동작");
		
		return "A Service 안녕";
	}
}
