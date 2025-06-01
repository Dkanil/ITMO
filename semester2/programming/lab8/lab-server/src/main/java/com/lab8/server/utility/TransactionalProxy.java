package com.lab8.server.utility;

import com.lab8.common.utility.ExecutionStatus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * Прокси для обработки аннотации @Transactional.
 */
public class TransactionalProxy implements InvocationHandler {
    private final Object target;
    private final Connection connection;

    public TransactionalProxy(Object target, Connection connection) {
        this.target = target;
        this.connection = connection;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Connection connection) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionalProxy(target, connection)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Transactional.class)) {
            try {
                connection.setAutoCommit(false);
                Object result = method.invoke(target, args);
                connection.commit();
                return result;
            } catch (Exception e) {
                connection.rollback();
                if (method.getReturnType().equals(ExecutionStatus.class)) {
                    return new ExecutionStatus(false, "Ошибка транзакции: " + e.getMessage());
                }
                throw new RuntimeException("Аннотация @Transactional может использоваться только с методами, возвращающими ExecutionStatus", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } else {
            return method.invoke(target, args);
        }
    }
}