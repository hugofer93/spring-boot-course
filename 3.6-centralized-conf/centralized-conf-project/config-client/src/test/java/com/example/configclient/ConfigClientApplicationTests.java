package com.example.configclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "spring.config.import=optional:configserver:http://localhost:8888",
        "app.name=Test App",
        "app.description=Test",
        "app.shared.message=Test message",
        "app.shared.environment=test",
        "custom.greeting=Hello",
        "custom.version=1.0.0"
})
@SpringBootTest
class ConfigClientApplicationTests {

    @Test
    void contextLoads() {
    }
}
