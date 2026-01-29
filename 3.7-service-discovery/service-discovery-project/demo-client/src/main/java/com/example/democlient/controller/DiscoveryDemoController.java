package com.example.democlient.controller;

import com.example.democlient.service.GreetingClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Endpoint que demuestra el descubrimiento: descubre greeting-service en Eureka
 * y llama a su API sin conocer su URL.
 */
@RestController
@RequestMapping("/api")
public class DiscoveryDemoController {

    private final GreetingClientService greetingClientService;

    public DiscoveryDemoController(GreetingClientService greetingClientService) {
        this.greetingClientService = greetingClientService;
    }

    @GetMapping("/discover-and-call")
    public ResponseEntity<Map<String, Object>> discoverAndCall() {
        Map<String, Object> result = greetingClientService.callGreetingService();
        return ResponseEntity.ok(result);
    }
}
