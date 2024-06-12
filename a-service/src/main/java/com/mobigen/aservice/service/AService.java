package com.mobigen.aservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AService {
	public Object getTest() {
		log.info("A service getTest 동작");
		return "A Service 안녕";
	}
}
