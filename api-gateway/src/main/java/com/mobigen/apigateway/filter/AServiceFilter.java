package com.mobigen.apigateway.filter;

import io.opentracing.Span;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import io.opentracing.Tracer;


@Component
@Slf4j
public class AServiceFilter extends AbstractGatewayFilterFactory<AServiceFilter.Config> {
	@Autowired
	private Tracer tracer;
	
	public AServiceFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			
			log.info("AServiceFilter baseMessage>>>>>> {}", config.getBaseMessage());
			
			if (config.isPreLogger()) {
				log.info("AServiceFilter Start>>>>>> {}", exchange.getRequest());
			}
			
			Span parentSpan = tracer.scopeManager().activeSpan();
			Span spanPhase1 = tracer.buildSpan("spanPhase_1").asChildOf(parentSpan).start();
			spanPhase1.log("API Gateway 서비스");  // 로그1
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				if (config.isPostLogger()) {
					log.info("AServiceFilter End>>>>>> {}", exchange.getResponse());
					try {
						spanPhase1.log("API Gateway 서비스 END");  // 로그1
					} finally {
						spanPhase1.finish();
					}
				}
			}));
		});
	}
	
	@Data
	public static class Config {
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}
}
