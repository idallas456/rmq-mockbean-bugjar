package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
public class DemoApplicationTests extends AbstractTestNGSpringContextTests {

	@Test(priority = 1)
	public void contextLoads() {
	}

}
