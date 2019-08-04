package com.github.cuinipeng.utils;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/4 21:19
 * @description 使用 JNI(native) 访问底层操作系统
 *
 * @usage
 *  1. 编写带有native声明的方法的Java类
 *  2. 使用javac命令编译编写的Java类
 *  3. 使用javah -jni ***来生成后缀名为.h的头文件
 *  4. 使用C/C++实现本地方法
 *  5.将本地方法编写的文件生成动态链接库
 *
 * @compile
 *  1. javac -encoding utf-8 -d target/classes src/main/java/com/github/cuinipeng/utils/HelloNative.java
 *  2. javah -cp target/classes -d target/include -jni com.github.cuinipeng.utils.HelloNative
 */
public class HelloNative {

    static {
        System.loadLibrary("hello");
    }

    public native void hello(String name);
}
