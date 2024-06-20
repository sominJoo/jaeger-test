package com.mobigen.aservice.controller;

import com.mobigen.aservice.service.AService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample")
public class AController {
	private final AService aService;
	private static final Logger logger = LoggerFactory.getLogger(AController.class);
	
	private final OpenTelemetrySdk openTelemetry;
	
	@Value("${spring.application.name}")
	private String serviceName;
	
	@GetMapping("/info")
	private Object getTest(HttpServletRequest request) {
		TextMapPropagator propagator = openTelemetry.getPropagators().getTextMapPropagator();
		Context extractedContext = propagator.extract(Context.current(), request, new TextMapGetter<>() {
			@Override
			public Iterable<String> keys(HttpServletRequest carrier) {
				return carrier.getHeaderNames() != null ? Collections.list(carrier.getHeaderNames()) : Collections.emptyList();
			}
			
			@Override
			public String get(HttpServletRequest carrier, String key) {
				return carrier.getHeader(key);
			}
		});
		
		Tracer tracer = openTelemetry.getTracer(serviceName);
		Span span = tracer.spanBuilder("getTest").setParent(extractedContext).startSpan();
		span.addEvent("A service GetTest info 출력");
		
		// Make the span the current span
		try (Scope scope = span.makeCurrent()) {
			logger.info("User Micro Service Call. 서비스 로그 확인");  // 로그1
			return aService.getTest();
		} catch (Throwable t) {
			span.recordException(t);
			throw t;
		} finally {
			span.end();
		}
	}
	
	@GetMapping("/error")
	private Object getError(HttpServletRequest request) {
		TextMapPropagator propagator = openTelemetry.getPropagators().getTextMapPropagator();
		Context extractedContext = propagator.extract(Context.current(), request, new TextMapGetter<>() {
			@Override
			public Iterable<String> keys(HttpServletRequest carrier) {
				return carrier.getHeaderNames() != null ? Collections.list(carrier.getHeaderNames()) : Collections.emptyList();
			}
			
			@Override
			public String get(HttpServletRequest carrier, String key) {
				return carrier.getHeader(key);
			}
		});
		
		Tracer tracer = openTelemetry.getTracer(serviceName);
		Span span = tracer.spanBuilder("getTest").setParent(extractedContext).startSpan();
		span.addEvent("A service GetTest info 출력");
		
		// Make the span the current span
		try (Scope scope = span.makeCurrent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "********* 에러 발생 *********", null);
		} catch (Throwable t) {
			span.recordException(t);
			throw t;
		} finally {
			span.end();
		}
	}
}
