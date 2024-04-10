package com.archipio.notificationservice.config;

import static com.archipio.notificationservice.util.AMQPUtils.NOTIFICATION_BY_EMAIL_QUEUE_NAME;
import static com.archipio.notificationservice.util.AMQPUtils.NOTIFICATION_BY_EMAIL_ROUTING_KEY;
import static com.archipio.notificationservice.util.AMQPUtils.NOTIFICATION_EXCHANGE_NAME;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {

  @Bean
  public Queue notificationByEmailQueue() {
    return new Queue(NOTIFICATION_BY_EMAIL_QUEUE_NAME);
  }

  @Bean
  public Exchange notificationExchange() {
    return new DirectExchange(NOTIFICATION_EXCHANGE_NAME);
  }

  @Bean
  public Binding bindingNotificationByEmailQueue() {
    return BindingBuilder.bind(notificationByEmailQueue())
            .to(notificationExchange())
            .with(NOTIFICATION_BY_EMAIL_ROUTING_KEY)
            .noargs();
  }

  @Bean
  public MessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(converter());
    return rabbitTemplate;
  }
}