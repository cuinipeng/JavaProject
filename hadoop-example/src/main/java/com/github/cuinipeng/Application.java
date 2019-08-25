package com.github.cuinipeng;

import com.github.cuinipeng.es.ElasticsearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("\n\n");

        // new HBaseService().run();
        new ElasticsearchService().run();
    }
}
