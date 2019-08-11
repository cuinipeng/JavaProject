package com.github.cuinipeng.proxy.extra;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/11 23:19
 * @description CGLIB 实现动态代理
 */
public class CglibProxy implements MethodInterceptor {

    private static CglibProxy instance = new CglibProxy();
    private static Logger logger = LoggerFactory.getLogger(CglibProxy.class);

    // 单例模式
    private CglibProxy() {}

    public static CglibProxy getInstance() {
        return instance;
    }

    @Override
    public Object intercept(Object object, Method method,
                            Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        // object 是被 CGLIB 代理过的对象, 无法直接通过 JDK 那样的方式直接通过反射触发函数.
        Object result = methodProxy.invokeSuper(object, args);
        after();
        return result;
    }

    public void before() {
        logger.info("before function by cglib.");
    }

    public void after() {
        logger.info("after cglib by cglib.");
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> tClass) {
        return (T) Enhancer.create(tClass, this);
    }
}
