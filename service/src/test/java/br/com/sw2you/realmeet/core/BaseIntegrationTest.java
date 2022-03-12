package br.com.sw2you.realmeet.core;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;

import br.com.sw2you.realmeet.Application;
import br.com.sw2you.realmeet.api.ApiClient;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public abstract class BaseIntegrationTest {

    @Autowired
    private Flyway flyway;

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setup() throws Exception {
        setupFlyway();
        setupEach();
    }

    protected void setupEach() throws Exception {
    }

    protected void setLocalhostBasePath(ApiClient apiClient, String path) throws Exception {
        apiClient.setBasePath(new URL("http", "localhost", serverPort, path).toString());
    }

    private void setupFlyway() {
        flyway.clean();
        flyway.migrate();
    }

}