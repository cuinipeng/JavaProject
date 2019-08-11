package com.github.cuinipeng.proxy;

import com.github.cuinipeng.proxy.extra.BuyProxy;
import com.github.cuinipeng.proxy.extra.CglibProxy;
import com.github.cuinipeng.proxy.extra.JDKProxy;
import com.github.cuinipeng.proxy.impl.BuyService;
import com.github.cuinipeng.proxy.service.IBuyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/11 22:11
 * @description Java 静态/动态代理(CGLIB), 在不改变类的前提下, 增加额外功能
 */
public class MultiProxy {

    private static Logger logger = LoggerFactory.getLogger(MultiProxy.class);

    public void run() {
        logger.info("start multiproxy example");

        // 通过静态代理对象操作被代理的对象, 使被代理的对象增加额外的功能.
        // 代理少数对象可以使用
        logger.info("start static proxy");
        BuyProxy buyProxy = new BuyProxy();
        buyProxy.setTarget(new BuyService());
        buyProxy.buy("Lisa", "Apple");
        logger.info("end static proxy");

        logger.info("start jdk dynamic proxy");
        JDKProxy jdkProxy = new JDKProxy(new BuyService());
        IBuyService buyService1 = jdkProxy.getProxy();
        buyService1.buy("Hatcher", "Company");
        logger.info("end jdk dynamic proxy");

        logger.info("start cglib dynamic proxy");
        IBuyService buyService2 = CglibProxy.getInstance().getProxy(BuyService.class);
        buyService2.buy("David", "House");
        logger.info("end cglib dynamic proxy");

        logger.info("end multiproxy example");
    }
}
