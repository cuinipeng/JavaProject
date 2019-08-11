package com.github.cuinipeng.proxy.extra;

import com.github.cuinipeng.proxy.service.IBuyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/11 22:39
 * @description 静态代理实现
 */
public class BuyProxy implements IBuyService {

    private static Logger logger = LoggerFactory.getLogger(BuyProxy.class);
    private IBuyService target;

    public BuyProxy() {
    }

    public BuyProxy(IBuyService iBuyService) {
        this.target = iBuyService;
    }

    public IBuyService getTarget() {
        return this.target;
    }

    public void setTarget(IBuyService target) {
        this.target = target;
    }

    @Override
    public void buy(String user, String commodity) {
        if (target != null) {
            logger.info("please take your wallet before shopping.");
            target.buy(user, commodity);
            logger.info("do not forget anything after shopping.");
        }
    }
}
