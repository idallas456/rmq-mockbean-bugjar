package com.example.demo.config;

import com.example.demo.messaging.consumer.UserMessageConsumer;
import com.example.demo.messaging.message.UserCreatedMessage;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoMessagingConfig {

	public static final String USER_EXCHANGE = "user.exchange";
	public static final String USER_MESSAGING_QUEUE = "user.messaging";
	@Autowired
	private ConnectionFactory connectionFactory;

	@Bean
	public Queue userMessageQueue() {
		return new Queue(USER_MESSAGING_QUEUE);
	}

	@Bean
	public TopicExchange userExchange() {
		return new TopicExchange(USER_EXCHANGE);
	}

	@Bean
	public Binding userMessageBinding() {
		return BindingBuilder.bind(userMessageQueue()).to(userExchange()).with(UserCreatedMessage.class.getCanonicalName());
	}

	@Bean
	public MessageListenerAdapter userMessageListenerAdapter(UserMessageConsumer userMessageConsumer, MessageConverter messageConverter) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(userMessageConsumer, "handleUserCreatedMessage");
		messageListenerAdapter.setMessageConverter(messageConverter);

		return messageListenerAdapter;
	}

	@Bean
	public AbstractMessageListenerContainer userMessageListenerContainer(MessageListenerAdapter userMessageListenerAdapter, MessageConverter messageConverter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setMessageConverter(messageConverter);
		container.setQueues(userMessageQueue());
		container.setMessageListener(userMessageListenerAdapter);
		container.setConcurrentConsumers(2);

		return container;
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(MessageConverter messageConverter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(messageConverter);

		return template;
	}

}
