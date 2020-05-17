package com.xiexinhai8.statemachine.service;

import java.util.List;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xiexinhai8.statemachine.PersistStateMachineHandler;
import com.xiexinhai8.statemachine.config.OrderPersistStateChangeListener;
import com.xiexinhai8.statemachine.dao.OrderRepo;
import com.xiexinhai8.statemachine.entity.Order;
import com.xiexinhai8.statemachine.entity.OrderEvents;

@Component
@Transactional
public class OrderStateService {

	@Resource(name="orderPersistStateChangeListener")
	private OrderPersistStateChangeListener orderPersistStateChangeListener;
	
	@Resource(name="persistStateMachineHandler")
    private PersistStateMachineHandler persistStateMachineHandler;

	
	@PostConstruct
    private void initialize() {
		this.persistStateMachineHandler.addPersistStateChangeListener(orderPersistStateChangeListener);
    }

    @Autowired
    private OrderRepo repo;

    @Transactional(readOnly=true)
    public String listDbEntries() {
        List<Order> orders = repo.findAll();
        StringJoiner sj = new StringJoiner(",");
        for (Order order : orders) {
            sj.add(order.toString());
        }
        return sj.toString();
    }

    @Transactional(readOnly=false)
    public boolean change(int order, OrderEvents event) {
        Order o = repo.findByOrderId(order);
        return persistStateMachineHandler.handleEventWithState(MessageBuilder.withPayload(event).setHeader("order", order).build(), o.getStatus());
    }

}