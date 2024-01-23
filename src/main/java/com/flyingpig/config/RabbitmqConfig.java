package com.flyingpig.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    private final String host = "localhost";
    private final int port = 5672;
    private final String virtualHost = "/";
    private final String username = "flyingpig";
    private final String password = "Aa123456";

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }


    private final String QUEUE_NAME = "signIn-queue";

    @Bean
    public Queue signInQueue() {
        return new Queue(QUEUE_NAME);
    }
}