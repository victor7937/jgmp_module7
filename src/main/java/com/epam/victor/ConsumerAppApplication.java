package com.epam.victor;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.epam.victor.model.QueueConstants.*;

@SpringBootApplication
@EnableScheduling
public class ConsumerAppApplication {



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
	Queue notificationQueueRetry() {
		return QueueBuilder.durable(NOTIFICATION_QUEUE_RETRY)
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
		return BindingBuilder.bind(notificationQueue1()).to(notificationExchange()).with(ROUTING_KEY_1);
	}

	@Bean
	Binding binding2() {
		return BindingBuilder.bind(notificationQueue2()).to(notificationExchange()).with(ROUTING_KEY_2);
	}

	@Bean
	Binding bindingRetry() {
		return BindingBuilder.bind(notificationQueueRetry()).to(notificationExchange()).with(ROUTING_KEY_RETRY);
	}
}

