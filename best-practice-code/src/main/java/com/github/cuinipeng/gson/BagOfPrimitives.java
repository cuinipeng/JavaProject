package com.github.cuinipeng.gson;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/25 20:50
 * @description 基本类型组成的对象
 */
public class BagOfPrimitives {

    private int value1 = 1;
    private String value2 = "abc";
    // transient 标志该字段不被序列化
    private transient int value3 = 3;

    public BagOfPrimitives() {
        // no-args constructor
    }
}
