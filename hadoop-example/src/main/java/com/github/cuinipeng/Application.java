package com.github.cuinipeng;

import com.github.cuinipeng.hbase.HBaseService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info(String.format("Args: %s", args.toString()));

        testHbaseService();
    }

    public static void testHbaseService() {
        HBaseService hs = new HBaseService();
        List<String> columnFamily = new ArrayList<>(2);
        columnFamily.add("cf1");
        columnFamily.add("cf2");
        hs.createTable("test", columnFamily);

        logger.info(MessageFormat.format("{0}", hs.getAllTableNames()));
    }

}
