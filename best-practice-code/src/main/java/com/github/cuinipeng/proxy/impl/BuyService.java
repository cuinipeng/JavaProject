package com.github.cuinipeng.proxy.impl;

import com.github.cuinipeng.proxy.service.IBuyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/11 22:31
 * @description
 */
public class BuyService implements IBuyService {

    private static Logger logger = LoggerFactory.getLogger(BuyService.class);

    @Override
    public void buy(String user, String commodity) {
        logger.info(MessageFormat.format("{0} want to buy {1}.", user, commodity));
    }
}
