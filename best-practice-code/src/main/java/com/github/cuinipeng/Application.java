package com.github.cuinipeng;

import com.github.cuinipeng.kafka.Consumer;
import com.github.cuinipeng.kafka.Producer;
import com.github.cuinipeng.netty.NettyTCPClient;
import com.github.cuinipeng.netty.NettyTCPServer;
import com.github.cuinipeng.proxy.MultiProxy;
import com.github.cuinipeng.utils.JacksonUsage;
import com.github.cuinipeng.utils.ThreadPoolUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        logger.info("best practice code application");
        if (args.length >= 1) {
            String action = args[0];
            Application.dispatch(action);
        }
    }

    public static void dispatch(String action) {
        switch (action) {
            case "thread.pool":
                new ThreadPoolUsage().run();
                break;
            case "jackson":
                new JacksonUsage().run();
                break;
            case "kafka.producer":
                new Producer().run();
                break;
            case "kafka.consumer":
                new Consumer().run();
                break;
            case "netty.tcp.server":
                new NettyTCPServer("localhost", 8080).run();
                break;
            case "netty.tcp.client":
                new NettyTCPClient("localhost", 8080).run();
                break;
            case "proxy":
                new MultiProxy().run();
                break;
            default:
                logger.info("Nothing");

        }
    }
}
