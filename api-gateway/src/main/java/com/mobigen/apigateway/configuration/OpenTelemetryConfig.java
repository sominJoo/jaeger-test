package com.mobigen.apigateway.configuration;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {
	
	@Bean
	public OpenTelemetrySdk openTelemetry() {
		JaegerGrpcSpanExporter jaegerExporter = JaegerGrpcSpanExporter.builder()
				.setEndpoint("http://localhost:14250")
				.build();
		
		SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
				.setResource(Resource.getDefault().toBuilder()
						.put(ResourceAttributes.SERVICE_NAME, "api-gateway")
						.build())
				.build();
		
		OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
				.setTracerProvider(tracerProvider)
				.build();
		
		GlobalOpenTelemetry.set(openTelemetrySdk);
		
		return openTelemetrySdk;
	}
	
	@Bean
	public Tracer tracer() {
		return GlobalOpenTelemetry.get().getTracer("api-gateway");
	}
}
