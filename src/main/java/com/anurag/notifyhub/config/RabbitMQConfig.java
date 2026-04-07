package com.anurag.notifyhub.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public Queue notificationQueue() {
    return new Queue("notification-queue", true);
  }

  @Bean
  public Queue deadLetterQueue() {
    return new Queue("notification-dlq", true);
  }

  @Bean
  public DirectExchange notificationExchange() {
    return new DirectExchange("notification-exchange");
  }

  @Bean
  public Binding binding(Queue notificationQueue, DirectExchange notificationExchange) {
    return BindingBuilder.bind(notificationQueue).to(notificationExchange).with("notification-routing-key");
  }

}
