package com.github.cuinipeng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info(String.format("Args: %s", args.toString()));
    }

}
