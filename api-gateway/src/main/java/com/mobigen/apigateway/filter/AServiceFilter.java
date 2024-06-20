package com.mobigen.apigateway.filter;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AServiceFilter extends AbstractGatewayFilterFactory<AServiceFilter.Config> {
	@Autowired
	private OpenTelemetrySdk openTelemetry;
	
	public AServiceFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			log.info("AServiceFilter baseMessage>>>>>> {}", config.getBaseMessage());
			
			if (config.isPreLogger()) {
				log.info("AServiceFilter Start>>>>>> {}", exchange.getRequest());
			}
			Tracer tracer = openTelemetry.getTracer("gateway-service");
			
			// Span 생성
			Span span = tracer.spanBuilder("AServiceFilter").startSpan();
			span.addEvent("API Gateway 서비스");
			
			// Context 설정
			Context context = Context.current().with(span);
			
			// traceparent 및 tracestate 헤더에 추가
			ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
				openTelemetry.getPropagators().getTextMapPropagator().inject(context, httpHeaders, (carrier, key, value) -> carrier.set(key, value));
			}).build();
			
			return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("AServiceFilter End>>>>>> {}", exchange.getResponse());
					try {
						span.addEvent("API Gateway 서비스 END");
					} finally {
						span.end();
					}
				}
			}));
		};
	}
	
	@Data
	public static class Config {
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}
}
