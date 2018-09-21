package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static com.example.demo.config.DemoMessagingConfig.USER_MESSAGING_QUEUE;

@SpringBootTest
public class DemoApplicationTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private RabbitAdmin rabbitAdmin;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test(priority = 1)
	public void contextLoads() {
		logger.info("=============== CONSUMERS: " + rabbitAdmin.getQueueProperties(USER_MESSAGING_QUEUE).get(RabbitAdmin.QUEUE_CONSUMER_COUNT));
	}

}
