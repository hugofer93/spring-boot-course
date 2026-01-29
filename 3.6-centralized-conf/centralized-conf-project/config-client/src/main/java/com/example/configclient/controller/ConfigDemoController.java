package com.example.configclient.controller;

import com.example.configclient.config.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * Controlador de ejemplo que expone propiedades obtenidas del Config Server.
 * Sirve para verificar que la configuración centralizada se carga correctamente.
 */
@RestController
@RequestMapping("/api")
public class ConfigDemoController {

    private final AppProperties appProperties;

    @Value("${custom.greeting:fallback}")
    private String customGreeting;

    @Value("${custom.version:0.0.0}")
    private String customVersion;

    public ConfigDemoController(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/config-demo")
    public Map<String, Object> getConfigDemo() {
        var shared = appProperties.getShared();
        var features = appProperties.getFeatures();
        return Map.of(
                "appName", Objects.requireNonNullElse(appProperties.getName(), ""),
                "description", Objects.requireNonNullElse(appProperties.getDescription(), ""),
                "sharedMessage", shared != null ? Objects.requireNonNullElse(shared.getMessage(), "") : "",
                "sharedEnvironment", shared != null ? Objects.requireNonNullElse(shared.getEnvironment(), "") : "",
                "featuresEnabled", features != null && features.isEnabled(),
                "featuresMaxItems", features != null ? features.getMaxItems() : 0,
                "customGreeting", Objects.requireNonNullElse(customGreeting, ""),
                "customVersion", Objects.requireNonNullElse(customVersion, ""),
                "source", "Config Server (centralized)"
        );
    }
}
