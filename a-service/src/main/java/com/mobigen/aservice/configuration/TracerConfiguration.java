package com.mobigen.aservice.configuration;

import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.extension.trace.propagation.JaegerPropagator;
import io.opentelemetry.opentracingshim.OpenTracingShim;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.semconv.ResourceAttributes;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.TimeUnit;

@Configuration
public class TracerConfiguration {
	@Value("${jaeger.endpoint}")
	private String jaegerEndpoint;
	
	@Value("${jaeger.timeout}")
	private int jaegerTimeout;
	
	@Value("${spring.application.name}")
	private String serviceName;
	
	@Bean
	public Tracer tracer() {
		Resource serviceNameResource =
				Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, serviceName));

		JaegerGrpcSpanExporter jaegerExporter = JaegerGrpcSpanExporter.builder()
				.setEndpoint(jaegerEndpoint)
				.setTimeout(jaegerTimeout, TimeUnit.SECONDS)
				.build();
		
		SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(SimpleSpanProcessor.create(jaegerExporter))
				.setResource(Resource.getDefault().merge(serviceNameResource))
				.build();
		
		OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
				.setPropagators(ContextPropagators.create(
						TextMapPropagator.composite(
								W3CTraceContextPropagator.getInstance(),
								JaegerPropagator.getInstance()
						)
				))
				.setTracerProvider(tracerProvider)
				.build();

		Runtime.getRuntime().addShutdownHook(new Thread(tracerProvider::close));
		return OpenTracingShim.createTracerShim(openTelemetry);
	}
}
