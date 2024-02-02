package com.epam.victor;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConsumerAppApplication {

	public static final String DLX_NOTIFICATION_EXCHANGE = "dlx_notification_exchange";
	public static final String NOTIFICATIONS_DLQ = "notifications_dlq";
	public static final String NOTIFICATION_EXCHANGE = "notification_exchange";
	public static final String NOTIFICATION_QUEUE1 = "notification_queue1";
	public static final String NOTIFICATION_QUEUE2 = "notification_queue2";

	public static void main(String[] args) {
		SpringApplication.run(ConsumerAppApplication.class, args);
	}


	@Bean
	Queue notificationQueue1() {
		return QueueBuilder.durable(NOTIFICATION_QUEUE1)
				.withArgument("x-dead-letter-exchange", DLX_NOTIFICATION_EXCHANGE)
				.build();
	}

	@Bean
	Queue notificationQueue2() {
		return QueueBuilder.durable(NOTIFICATION_QUEUE2)
				.withArgument("x-dead-letter-exchange", DLX_NOTIFICATION_EXCHANGE)
				.build();
	}


	@Bean
	FanoutExchange deadLetterExchange() {
		return new FanoutExchange(DLX_NOTIFICATION_EXCHANGE);
	}

	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(NOTIFICATIONS_DLQ).build();
	}

	@Bean
	Binding deadLetterBinding() {
		return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange());
	}

	@Bean
	DirectExchange notificationExchange() {
		return new DirectExchange(NOTIFICATION_EXCHANGE, true, false);
	}

	@Bean
	Binding binding1() {
		return BindingBuilder.bind(notificationQueue1()).to(notificationExchange()).with("notification1");
	}

	@Bean
	Binding binding2() {
		return BindingBuilder.bind(notificationQueue2()).to(notificationExchange()).with("notification2");
	}
}
