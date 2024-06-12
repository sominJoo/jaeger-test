package com.mobigen.apigateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AServiceFilter extends AbstractGatewayFilterFactory<AServiceFilter.Config> {
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
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				if (config.isPostLogger()) {
					log.info("AServiceFilter End>>>>>> {}", exchange.getResponse());
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
