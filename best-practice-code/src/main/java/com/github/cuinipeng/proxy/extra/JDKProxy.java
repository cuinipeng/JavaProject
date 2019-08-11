package com.github.cuinipeng.proxy.extra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/11 22:55
 * @description JDK 动态代理
 */
public class JDKProxy implements InvocationHandler {

    private static Logger logger = LoggerFactory.getLogger(JDKProxy.class);
    private Object target;

    public JDKProxy() {
    }

    public JDKProxy(Object target) {
        this.target = target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    public void before() {
        logger.info("before function by jdk.");
    }

    public void after() {
        logger.info("after function by jdk.");
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this);
    }
}
