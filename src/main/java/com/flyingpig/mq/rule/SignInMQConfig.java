package com.flyingpig.mq.rule;

import com.flyingpig.dataobject.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.flyingpig.dataobject.constant.RabbitMQConstants.SIGNIN_QUEUE_NAME2;

@Configuration
public class SignInMQConfig {
    // 交换机
    @Bean
    public FanoutExchange signInFanoutExchange() {
        return new FanoutExchange(RabbitMQConstants.SIGNIN_EXCHANGE_NAME);
    }

    // 队列1
    @Bean
    public Queue signInQueue1() {
        return new Queue(RabbitMQConstants.SIGNIN_QUEUE_NAME1);
    }

    // 队列2
    @Bean
    public Queue signInQueue2() {
        return new Queue(SIGNIN_QUEUE_NAME2);
    }

    // 绑定队列1和交换机
    @Bean
    public Binding signInBindingQueue1(Queue signInQueue1, FanoutExchange signInDirectExchange) {
        return BindingBuilder.bind(signInQueue1).to(signInDirectExchange);
    }

    // 绑定队列2和交换机
    @Bean
    public Binding signInBindingQueue2(Queue signInQueue2, FanoutExchange signInDirectExchange) {
        return BindingBuilder.bind(signInQueue2).to(signInDirectExchange);
    }
}

