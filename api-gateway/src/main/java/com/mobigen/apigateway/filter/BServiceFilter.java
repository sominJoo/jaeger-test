package com.mobigen.apigateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BServiceFilter extends AbstractGatewayFilterFactory<BServiceFilter.Config> {
	public BServiceFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("BServiceFilter baseMessage>>>>>> {}", config.getBaseMessage());
			if (config.isPreLogger()) {
				log.info("BServiceFilter Start>>>>>> {}", exchange.getRequest());
			}
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				if (config.isPostLogger()) {
					log.info("BServiceFilter End>>>>>> {}", exchange.getResponse());
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
