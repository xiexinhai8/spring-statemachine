package com.xiexinhai8.statemachine.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xiexinhai8.statemachine.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Integer> {


    Order findByOrderId(Integer order);
}