package com.xiexinhai8.statemachine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.xiexinhai8.statemachine.dao.OrderRepo;
import com.xiexinhai8.statemachine.entity.Order;
import com.xiexinhai8.statemachine.entity.OrderEvents;
import com.xiexinhai8.statemachine.entity.OrderStatus;
import com.xiexinhai8.statemachine.listener.PersistStateChangeListener;

@Component("orderPersistStateChangeListener")
public class OrderPersistStateChangeListener implements PersistStateChangeListener {
    @Autowired
    private OrderRepo repo;

	@Override
	public void onPersist(State<OrderStatus, OrderEvents> state, Message<OrderEvents> message,
			Transition<OrderStatus, OrderEvents> transition,
			StateMachine<OrderStatus, OrderEvents> stateMachine) {
		if (message != null && message.getHeaders().containsKey("order")) {
            Integer order = message.getHeaders().get("order", Integer.class);
            Order o = repo.findByOrderId(order);
            OrderStatus status = state.getId();
            o.setStatus(status);
            repo.save(o);
        }
	}
}