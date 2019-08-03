### JDK 发布历史
* JDK1.4 正则表达式，异常链，NIO，日志类，XML解析器，XLST转换器(2002-02-13)
* JDK1.5 自动装箱、泛型、动态注解、枚举、可变长参数、遍历循环(2004-09-30)
* JDK1.6 提供动态语言支持、提供编译API和卫星HTTP服务器API，改进JVM的锁，同步垃圾回收，类加载(2006-04)
* JDK1.7 提供GI收集器、加强对非Java语言的调用支持JSR-292,升级类加载架构(2011-07-28)
* JDK1.8 以 lambda 表达式和流的形式引入了函数式编程、方法引用、默认方法、新工具、Stream API、Date Time API 、Optional 类、Nashorn(JavaScript 引擎)(2014-03-18)
* Java9.0 Java 平台模块系统的引入、Jshell、集合，Stream,Optional增强、进程 API、平台日志 API 和 服务、反应式流(Reactive Streams)、变量句柄(2017-09-21)
* Java10.0 		2018-03-21
* Java11.0 		2018-09-2


[Java 8](https://juejin.im/post/5abc9ccc6fb9a028d6643eea)

* Lamdba 表达式语法
```
(params) -> exporession
(params) -> statement
(params) -> { statements }
```

* 用 lambda 表达式实现 Runable接口
```java
// Java 8 之前(匿名类)
new Thread(new Runable() {
    @Override
    public void run() {
        System.out.println("CODE RUNNING");
    }
}).start();
// Java 8(Lambda表达式)
new Thread(
    () -> System.out.println("CODE RUNNING");
).start();
```

* 使用 Lambda 迭代列表
```java
List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
features.forEach(System.out::println);
```

* 函数式接口(使Lambda表达式调用更简单)
```java
java.lang.FunctionalInterface
java.util.function.*    // 提供 43 个默认接口
java.util.Comparator
java.util.concurrent.Callable
java.lang.Runable

@FunctionalInterface
public interface Runable {
    public abstrace void run();
}
```

* 方法引用(通过::引用,就是Lambda表达式)
```java
Collections.sort(stringArray, (String s1, String s2) -> s1.compareToIgnoreCase(s2));
```

* Date and Time API
```java
/**
 * 1. 所有的类都是不可变的,对多线程环境有好处》
 * 2. Date/Time/DateTime/Timestamp/Timezone分离, 都有独立的format和parse方法
 */
LocalDate localDate = LocalDate.now();
LocalDate.of(2019, 08, 03);
LocalDate.parse("20190-08-03");
LocalDate.now().plusDays(1);    // 日期计算 +1 天

List<String> allZoneIds = new ArrayList<String>(ZoneId.getAvailableZoneIds());
Collections.sort(allZoneIds, (String s1, String s2) -> s1.compareToIgnoreCase(s2));

// ZoneId zoneId = ZoneId.of("Asia/Shanghai");
ZoneId zoneId = ZoneId.of("UTC");
LocalDateTime localDateTime = LocalDateTime.now();
ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
System.out.println(localDateTime);
System.out.println(zonedDateTime);

```

[Java 9](https://www.ibm.com/developerworks/cn/java/the-new-features-of-Java-9/index.html)
> 在引入了模块系统之后，JDK 被重新组织成 94 个模块。Java 应用可以通过新增的 jlink 工具，创建出只包含所依赖的 JDK 模块的自定义运行时镜像。这样可以极大的减少 Java 运行时环境的大小。

* Java 进程管理
```java
/**
 * Java 9 增加了 ProcessHandle 接口, 可以对原生进程进行管理, 尤其适合于管理长时间运行的进程. 
 * 在使用 ProcessBuilder 来启动一个进程之后, 可以通过 Process.toHandle()方法来得到一个
 * ProcessHandle 对象的实例. 通过 ProcessHandle 可以获取到由 ProcessHandle.Info 表示的进程的
 * 基本信息, 如命令行参数、可执行文件路径和启动时间等. ProcessHandle 的 onExit()方法返回一个
 * CompletableFuture<ProcessHandle>对象,可以在进程结束时执行自定义的动作.
 */
final ProcessBuilder processBuilder = new ProcessBuilder("top").inheritIO(); 
final ProcessHandle processHandle = processBuilder.start().toHandle(); 
processHandle.onExit().whenCompleteAsync((handle, throwable) -> { 
    if (throwable == null) { 
        System.out.println(handle.pid()); 
    } else { 
        throwable.printStackTrace(); 
    } 
});

```
* Java 安全算法(SHA-3)
```java
/**
 * Java 9 新增了 4 个 SHA-3 哈希算法, SHA3-224、SHA3-256、SHA3-384 和 SHA3-512.
 * 另外也增加了通过 java.security.SecureRandom 生成使用 DRBG 算法的强随机数。
 */
import org.apache.commons.codec.binary.Hex; 
public class SHA3 { 
    public static void main(final String[] args) throws NoSuchAlgorithmException { 
        final MessageDigest instance = MessageDigest.getInstance("SHA3-224"); 
        final byte[] digest = instance.digest("".getBytes()); 
        System.out.println(Hex.encodeHexString(digest)); 
    } 
}
```
* [jlink 创建自定义运行时映像](https://blog.idrsolutions.com/2017/05/java-9-jlink-explained-in-5-minutes/)
```shell
# 1. 检查模块
$ java --list-modules
```


[Java 10](https://www.ibm.com/developerworks/cn/java/j-5things17/index.html)

* 局部变量类型推断
* 增加、删除和弃用的特性
* 改进的容器感知
     部署到像 Docker 这样的容器中,JVM 现在能感知到它在一个容器中运行,
   并向该容器查询可供使用的处理器数量, 而不是向主机操作系统查询.
   也可以从外部连接到在容器中运行的 Java 进程, 这使得监控 JVM 进程
   变得更容易.可以使用 -XX:ActiveProcessorCount 标志, 显式指定容器化的
   JVM 能看到的处理器数量.
* 应用程序类数据共享

```java
String name = "Alex";
var name = "Alex";

// 用在 for 循环中
for(var book : books){} for(var i = 0; i < 10; i++){}
       
String message = "Incy wincy spider...";
StringReader reader = new StringReader(message);
StreamTokenizer tokenizer = new StreamTokenizer(reader);
var message = "Incy wincy spider...";
var reader = new StringReader(message);
var tokenizer = new StreamTokenizer(reader); // 分词器

# var 无法与 lambda 结合使用
```

