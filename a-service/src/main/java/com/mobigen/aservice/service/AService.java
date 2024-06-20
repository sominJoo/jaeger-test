package com.mobigen.aservice.service;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;


@Slf4j
@Service
@RequiredArgsConstructor

public class AService {
	private static final Logger logger = LoggerFactory.getLogger(AService.class);
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	private final  OpenTelemetrySdk openTelemetry;
	
	
	public Object getTest(HttpServletRequest request) {
		Context parentContext = getParantContext(request);
		
		Tracer tracer = openTelemetry.getTracer(applicationName);
		Span span = tracer.spanBuilder("getTest").setParent(parentContext).startSpan();
		span.addEvent("A service GetTest info 출력");
		
		try (Scope scope = span.makeCurrent()) {
			return "A Service 안녕";
		} catch (Throwable t) {
			span.recordException(t);
			throw t;
		} finally {
			span.end();
		}
	}
	public void getError(HttpServletRequest request) {
		Context parentContext = getParantContext(request);
		Tracer tracer = openTelemetry.getTracer(applicationName);
		Span span = tracer.spanBuilder("getTest").setParent(parentContext).startSpan();
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

	
	private Context getParantContext(HttpServletRequest request) {
		TextMapPropagator propagator = openTelemetry.getPropagators().getTextMapPropagator();
		return propagator.extract(Context.current(), request, new TextMapGetter<>() {
			@Override
			public Iterable<String> keys(HttpServletRequest carrier) {
				return carrier.getHeaderNames() != null ? Collections.list(carrier.getHeaderNames()) : Collections.emptyList();
			}
			
			@Override
			public String get(HttpServletRequest carrier, String key) {
				return carrier.getHeader(key);
			}
		});
	}
}
