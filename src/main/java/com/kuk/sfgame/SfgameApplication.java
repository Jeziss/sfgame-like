package com.kuk.sfgame;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SfgameApplication {

	public static void main(String[] args) {
		// Load .env file and set system properties before Spring Boot starts
		// java-dotenv looks for .env file in the current working directory by default
		Dotenv dotenv = Dotenv.configure()
			.ignoreIfMissing() // Don't fail if .env is missing
			.load();
		
		// Set all .env variables as system properties so Spring can access them
		dotenv.entries().forEach(entry -> 
			System.setProperty(entry.getKey(), entry.getValue())
		);
		
		SpringApplication.run(SfgameApplication.class, args);
	}

}
