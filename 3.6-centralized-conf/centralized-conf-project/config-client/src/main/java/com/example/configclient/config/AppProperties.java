package com.example.configclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propiedades cargadas desde el Config Server (config-client.yml).
 * Demuestra el uso de configuración centralizada de forma type-safe.
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String description;
    private Shared shared = new Shared();
    private Features features = new Features();

    public static class Shared {
        private String message;
        private String environment;

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getEnvironment() { return environment; }
        public void setEnvironment(String environment) { this.environment = environment; }
    }

    public static class Features {
        private boolean enabled = true;
        private int maxItems = 10;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getMaxItems() { return maxItems; }
        public void setMaxItems(int maxItems) { this.maxItems = maxItems; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Shared getShared() { return shared; }
    public void setShared(Shared shared) { this.shared = shared; }
    public Features getFeatures() { return features; }
    public void setFeatures(Features features) { this.features = features; }
}
