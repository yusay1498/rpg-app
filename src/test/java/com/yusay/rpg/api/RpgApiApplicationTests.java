package com.yusay.rpg.api;

import com.yusay.rpg.api.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RpgApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
