package com.github.cuinipeng.gson;

import com.google.gson.Gson;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/25 19:57
 * @description https://github.com/google/gson
 */
public class GsonUsage {

    private static Logger logger = LoggerFactory.getLogger(GsonUsage.class);
    private Gson gson = null;

    public GsonUsage() {
        // 构造 gson 实例, 使用 JsonPrintFormatter 代替 JsonCompactFormatter
        // Compact Vs. Pretty Printing for JSON Output Format
        // gson = new GsonBuilder().setPrettyPrinting().create();

        gson = new Gson();
    }

    private void primitivesExample() {
        // 基本类型序列化
        String r1 = gson.toJson(1);
        String r2 = gson.toJson("abcd");
        String r3 = gson.toJson(new Long(10));
        int[] values = {1};
        String r4 = gson.toJson(values);

        logger.info(r1);
        logger.info(r2);
        logger.info(r3);
        logger.info(r4);

        // 基本类型反序列化
        int one1 = gson.fromJson(r1, int.class);
        Integer one2 = gson.fromJson(r1, Integer.class);
        Long one3 = gson.fromJson(r1, Long.class);
        Boolean flag = gson.fromJson("false", Boolean.class);
        String str1 = gson.fromJson("\"abc\"", String.class);
        String[] str2 = gson.fromJson("[\"abc\"]", String[].class);
    }

    public void objectExample() {
        // 对象序列化
        BagOfPrimitives obj1 = new BagOfPrimitives();
        String r1 = gson.toJson(obj1);

        logger.info(MessageFormat.format("BagOfPrimitives: {0}", r1));

        // 对象反序列化
        BagOfPrimitives obj2 = gson.fromJson(r1, BagOfPrimitives.class);
    }

    public void run() {
        primitivesExample();
        objectExample();
    }
}
