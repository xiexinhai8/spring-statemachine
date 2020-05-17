package com.xiexinhai8.statemachine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;

import com.xiexinhai8.statemachine.PersistStateMachineHandler;
import com.xiexinhai8.statemachine.entity.OrderEvents;
import com.xiexinhai8.statemachine.entity.OrderStatus;

@Configuration
public class OrderPersistHandlerConfig {

	
    @Autowired
    @Qualifier("orderStateMachineFactory")
    private StateMachineFactory<OrderStatus, OrderEvents> orderStateMachineFactory;

    @Bean
    //@Scope("")
    public PersistStateMachineHandler persistStateMachineHandler() {
        return new PersistStateMachineHandler(orderStateMachineFactory);
    }


}