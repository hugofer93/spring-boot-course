package com.example.democlient.service;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Servicio que demuestra el descubrimiento: obtiene instancias de "greeting-service"
 * desde Eureka y llama a una de ellas sin conocer host ni puerto.
 */
@Service
public class GreetingClientService {

    private static final String GREETING_SERVICE_ID = "greeting-service";

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    public GreetingClientService(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    /**
     * Descubre una instancia de greeting-service en Eureka y llama a GET /api/hello.
     */
    public Map<String, Object> callGreetingService() {
        List<ServiceInstance> instances = discoveryClient.getInstances(GREETING_SERVICE_ID);
        if (instances == null || instances.isEmpty()) {
            return Map.of(
                    "success", false,
                    "message", "No hay instancias de " + GREETING_SERVICE_ID + " registradas en Eureka"
            );
        }

        ServiceInstance instance = instances.get(0);
        String baseUrl = instance.getUri().toString();
        String url = baseUrl + "/api/hello";

        @SuppressWarnings("unchecked")
        Map<String, String> response = restTemplate.getForObject(url, Map.class);

        return Map.of(
                "success", true,
                "discoveredInstance", instance.getHost() + ":" + instance.getPort(),
                "greetingResponse", response != null ? response : Map.of()
        );
    }
}
