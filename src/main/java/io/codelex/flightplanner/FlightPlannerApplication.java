package io.codelex.flightplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication

public class FlightPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightPlannerApplication.class, args);
	}

}
