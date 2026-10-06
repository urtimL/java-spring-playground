package dev.timur.playground;

import dev.timur.playground.health.HealthStatusService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JavaSpringPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSpringPlaygroundApplication.class, args);
	}

	@Bean
    CommandLineRunner run(HealthStatusService healthStatusService) {
        return args -> {
            String status = healthStatusService.getStatus();
            System.out.println("CloudBooking health status: " + status);
        };
    }

}
