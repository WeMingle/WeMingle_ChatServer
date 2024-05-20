package com.wemingle_chat.chat.global.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private static final String CHAT_ROUTING_KEY = "*.room.*";
    private static final String PRESENCE_QUEUE_NAME = "presence.queue";
    private static final String PRESENCE_EXCHANGE_NAME = "presence.exchange";
    private static final String ONLINE_ROUTING_KEY = "user.online.#";

    //Queue 등록
    @Bean
    Queue chatQueue() {
        return QueueBuilder.durable(CHAT_QUEUE_NAME).build();
    }
    @Bean
    Queue onlineQueue() {
        return QueueBuilder.durable(PRESENCE_QUEUE_NAME).build();
    }

    //Exchange 등록
    @Bean
    TopicExchange chatExchange() {
        return ExchangeBuilder.topicExchange(CHAT_EXCHANGE_NAME).durable(true).build();
    }
    @Bean
    TopicExchange presenceExchange() {
        return ExchangeBuilder.topicExchange(PRESENCE_EXCHANGE_NAME).durable(true).build();
    }

    // Exchange와 Queue바인딩
    @Bean
    Binding binding(Queue chatQueue, TopicExchange chatExchange){
        return BindingBuilder
                .bind(chatQueue)
                .to(chatExchange)
                .with(CHAT_ROUTING_KEY);
    }
    @Bean
    Binding onlineBinding(Queue onlineQueue, TopicExchange presenceExchange) {
        return BindingBuilder
                .bind(onlineQueue)
                .to(presenceExchange)
                .with(ONLINE_ROUTING_KEY);
    }

    // RabbitMQ와의 메시지 통신을 담당하는 클래스
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // RabbitMQ와의 연결을 관리하는 클래스
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    // 메시지를 JSON형식으로 직렬화하고 역직렬화하는데 사용되는 변환기
    // RabbitMQ 메시지를 JSON형식으로 보내고 받을 수 있음
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
