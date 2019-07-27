https://mvnrepository.com/

### Java Logging Framework
https://stackify.com/compare-java-logging-frameworks

### Hibernate Logging Guide – Use the right config for development and production
https://thoughts-on-java.org/hibernate-logging-guide

1. SLF4J
Logger logger = LoggerFactory.getLogger(this.getClass().getName());
<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.26</version>
</dependency>

2. Apache Log4j
<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.26</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12 -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.26</version>
</dependency>
<!-- https://mvnrepository.com/artifact/log4j/log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```
log4j.properties
### 设置###
log4j.rootLogger = INFO,stdout,D,E

### 输出信息到控制抬 ###
log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %l: %m%n

### 输出DEBUG 级别以上的日志到 debug.log ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = debug.log
log4j.appender.D.Append = true
log4j.appender.D.Threshold = DEBUG
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %l: %m%n

### 输出ERROR 级别以上的日志到 error.log ###
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File = error.log
log4j.appender.E.Append = true
log4j.appender.E.Threshold = ERROR
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss,SSS} %-5p %l: %m%n

### 屏蔽其他 logger ###
log4j.logger.org.apache.hbase =INFO
```

3. Logback
<!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-core -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.2.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-classic -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/ch.qos.logback/logback-access -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-access</artifactId>
    <version>1.2.3</version>
</dependency>
```
logback.xml
<?xml version="1.0" encoding="UTF-8"?>
<!--每天生成一个文件, 归档文件保存30天-->
<configuration scan="true" scanPeriod="60" debug="false">
   <!--设置自定义logfmt属性-->
   <property name="logfmt" value="%d{HH:mm:ss.SSS} [%-5level] [%thread] [%logger] %msg%n"/>
   <!--控制台输出日志-->
   <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
      <!--设置控制台输出日志的格式-->
      <encoder>
         <pattern>${logfmt}</pattern>
      </encoder>
   </appender>
   <!--滚动记录日志文件-->
   <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!--当天生成的日志文件名称-->
      <file>app.log</file>
      <!--根据时间来记录日志文件-->
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
         <!--归档日志文件的名称-->
         <fileNamePattern>app-%d{yyyy-MM-dd}.log</fileNamePattern>
         <!--归档文件保存30天-->
         <maxHistory>30</maxHistory>
      </rollingPolicy>
      <!--生成的日志信息格式-->
      <encoder>
         <pattern>${logfmt}</pattern>
      </encoder>
   </appender>
   <!--根root logger-->
   <root level="DEBUG">
      <!--设置根logger的日志输出目的地-->
      <appender-ref ref="FILE"/>
      <appender-ref ref="CONSOLE"/>
   </root>
</configuration>
```

4. Apache Log4j2
```
log4j2.xml
```















<exclusions>
	<exclusion>
	    <groupId>org.slf4j</groupId>
	    <artifactId>slf4j-api</artifactId>
	</exclusion>
	<exclusion>
	    <groupId>junit</groupId>
	    <artifactId>junit</artifactId>
	</exclusion>
	<exclusion>
	    <groupId>log4j</groupId>
	    <artifactId>log4j</artifactId>
	</exclusion>
	<exclusion>
	    <groupId>commons-logging</groupId>
	    <artifactId>commons-logging</artifactId>
	</exclusion>
</exclusions>