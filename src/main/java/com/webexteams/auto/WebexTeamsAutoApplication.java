package com.webexteams.auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class WebexTeamsAutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebexTeamsAutoApplication.class, args);
	}


}
