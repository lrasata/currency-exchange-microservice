package com.lrasata.microservices.currencyexchangemicroservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
		
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
		
	@GetMapping("/sample-api")
	@Retry(name = "sample-api", fallbackMethod = "hardcodedResponse") // in case a service is down temporarily, retry
	//@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse") // docs https://resilience4j.readme.io/docs/circuitbreaker
	// Circuitbreaker breaks the circuit and use the fallbackMethod for a certain amount of time, then try again and depending on percentage of success decide if using fallbackMethod again
	//@RateLimiter(name="default") // for 10s I want to allow only 1000 class to the sample api
	@Bulkhead(name="sample-api")
	public String sampleApi() {
		logger.info("Sample api call received");
//		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", 
//		String.class);
//		return forEntity.getBody();
		return "sample-api";
	}
	
	public String hardcodedResponse(Exception ex) {
		return "fallback-response";
	}

}

