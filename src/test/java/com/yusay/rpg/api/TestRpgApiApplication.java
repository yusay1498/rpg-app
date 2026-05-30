package com.yusay.rpg.api;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestRpgApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(RpgApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
