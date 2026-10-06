package dev.timur.playground.health;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HealthStatusService {

    private final RestClient restClient;

    public HealthStatusService() {
        this.restClient = RestClient.create();
    }

    public HealthStatusService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public HealthStatusResponse getHealthStatusResponse() {
        return restClient
                .get()
                .uri("https://api.test.cloudbooking.io/api/v1/health/status")
                .retrieve()
                .body(HealthStatusResponse.class);
    }

    public String getStatus() {
        HealthStatusResponse response = getHealthStatusResponse();
        return response.getStatus();
    }
}