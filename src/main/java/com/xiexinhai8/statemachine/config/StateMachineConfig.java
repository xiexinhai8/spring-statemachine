package com.xiexinhai8.statemachine.config;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.xiexinhai8.statemachine.entity.OrderEvents;
import com.xiexinhai8.statemachine.entity.OrderStatus;

@Configuration
@EnableStateMachineFactory(name="orderStateMachineFactory")
/**
 * public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvents> {
 * 使用StateMachineConfigurerAdapter可以使用字符串代替枚举来匹配state和event
  */

public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvents> {

	@Override
	public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvents> states) throws Exception {
		states.withStates()
				// 定义初始状态
				.initial(OrderStatus.WAIT_SUBMIT)
				// 定义所有状态集合
				.states(EnumSet.allOf(OrderStatus.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvents> transitions) throws Exception {
		transitions.withExternal()
				.source(OrderStatus.WAIT_SUBMIT).target(OrderStatus.WAIT_REAL_NAME)
				.event(OrderEvents.UN_REAL_NAME)
				.and().withExternal()
				.source(OrderStatus.WAIT_SUBMIT).target(OrderStatus.WAIT_CHECK)
				.event(OrderEvents.SUBMITED)
				.and().withExternal()
				.source(OrderStatus.WAIT_SUBMIT).target(OrderStatus.WAIT_APPLICATION_FORM)
				.event(OrderEvents.PROXY_SUBMITED)
				.and().withExternal()
				.source(OrderStatus.WAIT_APPLICATION_FORM).target(OrderStatus.WAIT_CHECK)
				.event(OrderEvents.APPLICATION_FORM_SUBMITED)
				.and().withExternal()
				.source(OrderStatus.WAIT_REAL_NAME).target(OrderStatus.WAIT_SUBMIT)
				.event(OrderEvents.REAL_NAME);
	}
}