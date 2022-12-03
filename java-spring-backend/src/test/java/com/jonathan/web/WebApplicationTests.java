package com.jonathan.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class WebApplicationTests {

	@Test
	void contextLoads() {
	}
}
