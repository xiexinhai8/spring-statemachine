package com.xiexinhai8.statemachine;

import java.util.Iterator;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.listener.AbstractCompositeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.LifecycleObjectSupport;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.util.Assert;

import com.xiexinhai8.statemachine.entity.OrderEvents;
import com.xiexinhai8.statemachine.entity.OrderStatus;
import com.xiexinhai8.statemachine.listener.PersistStateChangeListener;

public class PersistStateMachineHandler extends LifecycleObjectSupport {

    private final StateMachineFactory<OrderStatus, OrderEvents> stateMachineFactory;
    private final PersistingStateChangeInterceptor interceptor = new PersistingStateChangeInterceptor();
    private final CompositePersistStateChangeListener listeners = new CompositePersistStateChangeListener();


    /**
     * 实例化一个新的持久化状态机Handler
     *
     * @param stateMachine 状态机实例
     */
    public PersistStateMachineHandler(StateMachineFactory<OrderStatus, OrderEvents> stateMachine) {
        Assert.notNull(stateMachine, "State machine must be set");
        this.stateMachineFactory = stateMachine;
    }

/*    @Override
    protected void onInit() throws Exception {
        stateMachineFactory.getStateMachineAccessor().doWithAllRegions(function -> function.addStateMachineInterceptor(interceptor));
    }*/


    /**
     * 处理entity的事件
     *
     * @param event
     * @param state
     * @return 如果事件被接受处理，返回true
     */
    public boolean handleEventWithState(Message<OrderEvents> event, OrderStatus state) {
        // stateMachineFactory.stop();
        StateMachine<OrderStatus, OrderEvents> stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.addStateMachineInterceptor(interceptor));
        List<StateMachineAccess<OrderStatus, OrderEvents>> withAllRegions = stateMachine.getStateMachineAccessor()
                .withAllRegions();
        for (StateMachineAccess<OrderStatus, OrderEvents> a : withAllRegions) {
            System.out.println("=====" + a);
            a.resetStateMachine(new DefaultStateMachineContext<>(state, null, null, null));
        }
        stateMachine.start();
        return stateMachine.sendEvent(event);
    }

    /**
     * 添加listener
     *
     * @param listener the listener
     */
    public void addPersistStateChangeListener(PersistStateChangeListener listener) {
        listeners.register(listener);
    }

    private class PersistingStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderStatus, OrderEvents> {

        // 状态预处理的拦截器方法
        @Override
        public void preStateChange(State<OrderStatus, OrderEvents> state, Message<OrderEvents> message,
                                   Transition<OrderStatus, OrderEvents> transition, StateMachine<OrderStatus,
                OrderEvents> stateMachine) {
            listeners.onPersist(state, message, transition, stateMachine);
        }
    }

    private class CompositePersistStateChangeListener extends AbstractCompositeListener<PersistStateChangeListener> implements
            PersistStateChangeListener {
        @Override
        public void onPersist(State<OrderStatus, OrderEvents> state, Message<OrderEvents> message,
                              Transition<OrderStatus, OrderEvents> transition, StateMachine<OrderStatus,
                OrderEvents> stateMachine) {
            for (Iterator<PersistStateChangeListener> iterator = getListeners().reverse(); iterator.hasNext(); ) {
                PersistStateChangeListener listener = iterator.next();
                listener.onPersist(state, message, transition, stateMachine);
            }
        }
    }
}