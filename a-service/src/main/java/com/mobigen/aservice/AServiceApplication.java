package com.mobigen.aservice;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AServiceApplication {
	
	public static void main(String[] args) {SpringApplication.run(AServiceApplication.class, args);}
}
