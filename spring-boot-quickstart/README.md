### 生成证书
keytool -genkey -keystore src/main/resources/https.keystore -keyalg RSA -alias springboot


### 检查当前可配置参数
https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html


### Spring Boot 中使用 Swagger
https://www.ibm.com/developerworks/cn/java/j-using-swagger-in-a-spring-boot-project/index.html
https://juejin.im/post/5b1922cdf265da6e173a51ec
https://localhost:8443/v2/api-docs
https://localhost:8443/swagger-ui.html


### Learn Spring 5 and Spring Boot 2
https://www.baeldung.com/learn-spring-course


https://www.baeldung.com/
  https://www.baeldung.com/spring-mvc



spring core annotations:
  org.springframework.beans.factory.annotation
  org.springframework.context.annotation packages
  # 依赖注入相关
  @Autowired -> 构造器/setter/field
  @Bean -> 必须在 @Configuration 注解的类中
  @Qualifier -> 和 @Autowired 注解配合使用(多个bean实例满足要求场景)
  @Required -> setter 要求通过 xml 注入
  @Value -> 注入一个值(构造器/setter/field)
  @DependOn -> 显式依赖注入
  # 上下文配置相关
  @Profile
  @Import
  @ImportResource
  @PropertySource
  @PropertySources

spring web annotations:
  org.springframework.web.bind.annotation
  @RequestMapping
  @GetMapping
  @PostMapping
  @PutMapping
  @DeleteMapping
  @PatchMapping
  @RequestBody -> 映射 http body 到 Object(反序列化类型依赖于内容类型)
  @PathVariable -> 绑定方法参数到 URI 模板变量
  @RequestParam -> 访问 http 请求参数
  @CookieValue -> 访问 cookie
  @RequestHeader -> 访问 http headers
  @ResponseBody -> Spring 对待 request handler 的返回值作为 response 本身
  @ExceptionHandler -> 异常捕获
  @ResponseStatus -> 配合 ExceptionHandler 一起处理异常
  @Controller
  @RestController -> Controller + ResponseBody
  @ModelAttribute -> 访问已经在 MVC model 中的元素
  @CrossOrigin

spring boot annotations:
  org.springframework.boot.autoconfigure
  org.springframework.boot.autoconfigure.condition
  @SpringBootApplication -> @Configuration + @EnableAutoConfiguration + @ComponentScan
  @ConditionalOnClass
  @ConditionalOnMissingClass
  @ConditionalOnBean
  @ConditionalOnMissingBean
  @ConditionalOnProperty
  @ConditionalOnResource
  @ConditionalOnWebApplication
  @ConditionalOnNotWebApplication
  @ConditionalExpression
  @Conditional

spring scheduling annotations:
  org.springframework.scheduling.annotation
  @EnableAsync
  @Async
  @EnableScheduling
  @Scheduled

spring bean annotations:

