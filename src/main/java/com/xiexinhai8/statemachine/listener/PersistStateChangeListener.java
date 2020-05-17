package com.xiexinhai8.statemachine.listener;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import com.xiexinhai8.statemachine.entity.OrderEvents;
import com.xiexinhai8.statemachine.entity.OrderStatus;

/**
 * 可以通过 addPersistStateChangeListener，增加当前Handler的PersistStateChangeListener。
 * 在状态变化的持久化触发时，会调用相应的实现了PersistStateChangeListener的Listener实例。
 */
public interface PersistStateChangeListener {

        /**
         * 当状态被持久化，调用此方法
         *
         * @param state
         * @param message
         * @param transition
         * @param stateMachine 状态机实例
         */
        void onPersist(State<OrderStatus, OrderEvents> state, Message<OrderEvents> message, Transition<OrderStatus,
                OrderEvents> transition,
                       StateMachine<OrderStatus, OrderEvents> stateMachine);
    }