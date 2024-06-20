package com.mobigen.bservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BService {
	public Object getTest() {
		log.info("B service getTest 동작");
		return "B service 안녕";
	}
}
