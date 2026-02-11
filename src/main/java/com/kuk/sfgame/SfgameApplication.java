package com.kuk.sfgame;

import io.github.cdimascio.dotenv.Dotenv;
import io.vavr.collection.List.Cons;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kuk.sfgame.util.Calculation;
import com.kuk.sfgame.util.Constants;

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
		
		
		/*
		for (int i = 1; i < 100; i++) {
			int cost = Calculation.goldAttributeCost(i);
			System.out.println("Gold needed to upgrade attribute at level " + i + ": " + cost);
		} 
		
		for (int i = 1; i < 100; i++) {
			System.out.println("Level: " + i);
			System.out.println("XP needed to reach level " + i + ": " + Calculation.calculateXPForLevelUp(i));

			System.out.println("XP range in the tavern: " + Calculation.calculateTavernXPmin(i, 0) + " - " + Calculation.calculateTavernXPmax(i, 0));
			System.out.println("Gold range in the tavern: " + Calculation.calculateTavernGoldMin(i, 0) + " - " + Calculation.calculateTavernGoldMax(i, 0));
		}

		for (int i = 1; i <= 100; i++) {
			System.out.println("Gold curve na levelu " + i + ": " + Constants.GOLD_CURVE[i]); // Example gold curve formula
			//System.out.println("Base gold for level " + i + ": " + baseGold);
		}

		for (int i = 1; i <= 100; i++) {
			System.out.println("Base gold na levelu " + i + ": " + Calculation.calculateBaseGold(i)); // Example gold curve formula
			//System.out.println("Base gold for level " + i + ": " + baseGold);
		} */
	}

}
