# SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程

写作时间：2018-12-20
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA, 
# Spring Boot 2.1的好处
`Spring Boot`提供了快速方式去简历应用. 应用的`classpath`和`beans` 已默认配置好, `Spring Boot` 已经搭建好架构基础设施，你可以专注于业务开发. 

举栗子🌰:
* 想要`Spring MVC`? 你需要设置相关`beans`, `Spring Boot` 已经自动设置好. `Spring MVC app` 需要 `servlet container`, `Spring Boot` 自动配置并内置了`Tomcat`.

* 想要 `Jetty`容器? 要是这样, 你不需要`Tomcat`, 你需要内置的是`Jetty`. `Spring Boot`也帮你内置了`Jetty`容器.

* 想要`Thymeleaf`? 相关`beans`需要设置在`application context`; `Spring Boot`已经为你设置好.

`Spring Boot`还可以定制化，比如, `Thymeleaf`已经被用到, `Spring Boot`自动加入`SpringTemplateEngine`在你的`application context` . 但是如果你自定义了`SpringTemplateEngine`(设置不用默认方式), 那么 `Spring Boot`就不做处理，把控制权留给你.

下面将一步一步实现第一个Spring Boot工程，本系列教材都是基于`2.1`版本。

# 创建一个简单web application
创建`Web application`流程： 
打开Idea-> create new Project ->Spring Initializr ->填写group、artifact ->钩上web(开启web功能）->点下一步就行了。(下面有具体步骤图片)
1. create new Project
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220085346453.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

2. Spring Initializr
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220085529130.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

3. 填写group、artifact
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220090055947.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

4. 钩上web(开启web功能）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220090318149.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

5. 就这么简单，`Spring Boot`工程建立好了。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220090810675.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

# 在简单的`web application`创建一个`web controller` .
`Controller` 规范是放在package `web`中
路径： `src/main/java/web/HelloController.java`
```java
package web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
```
`HelloController`上面的注解`@RestController`, 表示用`Spring MVC`去处理`web requests`. `@RequestMapping`设置默认访问路径`/`, 将由方法`index()`处理. 用浏览器访问`http://localhost:8080/` 或者在`command line`中用命令`curl ` + ` http://localhost:8080/` , 将返回一个纯文本. 这是因为注解`@RestController`有 `@Controller` 和 `@ResponseBody`组合而成, 这两个注解表示请求结果返回**data**，而不是**view**. 点击看源码就一清二楚：
```java
package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
    @AliasFor(
        annotation = Controller.class
    )
    String value() default "";
}
```
# 创建`Application class`
删掉默认创建了`Application class`,  `com.zgpeace.demo1boot.Demo1bootApplication.java`
新建`web.Application.java`
代码如下：
```java
package web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Demo1bootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Demo1bootApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName: beanNames) {
                System.out.println(beanName);
            }
        };
    }

}
```
`@SpringBootApplication`是个方便的注解，包含了一下内容:

* `@SpringBootConfiguration`包含了`@Configuration` ，表示该类作为定义`bean`的`application context`.

* `@EnableAutoConfiguration` 表示`Spring Boot`把 `classpath`下的`beans`, `Spring Boot`配置的`beans`, 和各种各样的`property`设置，都加载进来.

* 通常`Spring MVC app`需要加注解`@EnableWebMvc`, `Spring Boot` 自动加这个注解当它看到`classpath`包含`spring-webmvc`. 这个标识该工程为`web application`, 并激活`key behaviors`(比如设置`DispatcherServlet`).

* `@ComponentScan`通知`Spring`在**Controller**当前的**package**里去寻找其它`components`, `configurations`, `services`, 允许它找到controllers.

`main()` 方法启动了`Spring Boot`的`SpringApplication.run()`方法去启动一个`application`. 你会发现不需要任何XML配置文件? 也没有**web.xml** . 这个`web application`是个100%纯`Java`.

`CommandLineRunner` 方法标识为`@Bean`, 并在启动时运行. 它检索所有的`beans`, 有的是工程创建的类，有的是`Spring Boot`自动引入. 排序并打印输出.

把默认的main class删掉以后，发现运行有错误，点击Edit Configuration
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220103733294.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

选择为新建类`web.Application`
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220103835850.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

# 运行application
1. 点击右上角的运行按钮：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220101949540.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

如果用Maven，cd到工程的根目录，运行命令:
```shell
mvn package && java -jar target/demo1boot-0.0.1-SNAPSHOT.jar
```
2. 可以看到控制台结果输出：
```shell
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.1.RELEASE)

2018-12-20 10:21:15.445  INFO 5901 --- [           main] c.z.demo1boot.Demo1bootApplication       : Starting Demo1bootApplication on zgpeaces-MacBook-Pro.local with PID 5901 (/Users/zgpeace/Intellij-workspace/demo1boot/target/classes started by zgpeace in /Users/zgpeace/Intellij-workspace/demo1boot)
2018-12-20 10:21:15.450  INFO 5901 --- [           main] c.z.demo1boot.Demo1bootApplication       : No active profile set, falling back to default profiles: default
2018-12-20 10:21:16.201  INFO 5901 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2018-12-20 10:21:16.217  INFO 5901 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2018-12-20 10:21:16.218  INFO 5901 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/9.0.13
...
Let's inspect the beans provided by Spring Boot:
application
beanNameHandlerMapping
defaultServletHandlerMapping
dispatcherServlet
embeddedServletContainerCustomizerBeanPostProcessor
handlerExceptionResolver
helloController
httpRequestHandlerAdapter
messageSource
mvcContentNegotiationManager
mvcConversionService
mvcValidator
org.springframework.boot.autoconfigure.MessageSourceAutoConfiguration
org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration$DispatcherServletConfiguration
org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration$EmbeddedTomcat
org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration
org.springframework.boot.context.embedded.properties.ServerProperties
org.springframework.context.annotation.ConfigurationClassPostProcessor.enhancedConfigurationProcessor
org.springframework.context.annotation.ConfigurationClassPostProcessor.importAwareProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalRequiredAnnotationProcessor
org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration
propertySourcesBinder
propertySourcesPlaceholderConfigurer
requestMappingHandlerAdapter
requestMappingHandlerMapping
resourceHandlerMapping
simpleControllerHandlerAdapter
tomcatEmbeddedServletContainerFactory
viewControllerHandlerMapping
...
```
打印输出可以看到`org.springframework.boot.autoconfigure` , `tomcatEmbeddedServletContainerFactory`.

在控制台访问链接：
```shell
$ curl localhost:8080
Greetings from Spring Boot!
```
# 增加单元测试 Unit Tests
pom.xml默认引入了Unit Tests依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```
新增类 `src/test/java/web/HelloControllerTest.java`
```java
package web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!")));
    }

}
```
`MockMvc`来自于**Spring Test**，允许你方便的编译类, 发送HTTP请求给`DispatcherServlet`, 并断言结果**assertions**. 用`@AutoConfigureMockMvc` 和`@SpringBootTest`去注入`MockMvc` 实例. 例子用`@SpringBootTest`去创建了整个`application context`. 轻量级的选择用注解`@WebMvcTest`,制动web层的`application context`.

运行单元测试，可以在`HelloController`的'index()'方法里面打个断点，测试通过：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220114205695.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

第二种方式模拟HTTP请求周期，可以用`Spring Boot`去实现网络请求整个流程的集成测试:
新建类： `src/test/java/web/HelloControllerIT.java`
```java
package web;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void getHello() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), equalTo("Greetings from Spring Boot!"));
    }

}
```
内置的Tomcat启动用了一个随机的端口`webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT` , 实际的端口在运行时runtime显示`@LocalServerPort`.

# 增加 production-grade services
项目都需要监控服务的状态，**Spring Boot** 提供了很多**actuator module**，比如**health, audits, beans**.
在**pom.xml**里面增加如下依赖
```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
```
如果依赖没有生效，可能是没有下载构建成功，可以手动mvn clean, mvn build一遍。
![在这里插入图片描述](https://img-blog.csdnimg.cn/2018122012161068.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

点击右上角的运行按钮：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181220101949540.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

`actuator`里面的组件概览:
```shell
$ curl localhost:8080/actuator
{"_links":
	{"self":
		{"href":"http://localhost:8080/actuator","templated":false},
		"health":{"href":"http://localhost:8080/actuator/health","templated":false},
		"health-component":{"href":"http://localhost:8080/actuator/health/{component}",
		"templated":true
	},
	"health-component-instance":{"href":"http://localhost:8080/actuator/health/{component}/{instance}","templated":true},
	"info":{"href":"http://localhost:8080/actuator/info","templated":false}
	}
}
```
检查应用的`health`
```shell
$ curl localhost:8080/actuator/health
{"status":"UP"}
```
检查`shutdown`，因为没有启用，比较友好的提示：
```shell
% curl -X POST localhost:8080/actuator/shutdown
{"timestamp":"2018-12-20T05:46:41.856+0000","status":404,
"error":"Not Found","message":"No message available",
"path":"/actuator/shutdown"}%  
```

# 查看 Spring Boot’s starters
已经看了一些 Spring Boot’s "starters". [你可以点击此查看全部.](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters)

# JAR支持 和 Groovy支持
Spring Boot支持单独的WAR包发布，也可以打包成可执行的jar包，上面Maven运行有例子。默认打包方式支持Maven和Gradle, 内置的插件`spring-boot-maven-plugin`和`spring-boot-gradle-plugin` .

**Spring Boot**还支持**Groovy**, 你可以用单个Groovy文件构建**Spring MVC web apps**.

创建文件`ThisWillActuallyRun.groovy` , 实现代码如下:
```java
@RestController
class ThisWillActuallyRun {

    @RequestMapping("/")
    String home() {
        return "Hello World!"
    }

}
```
接下来安装 [Spring Boot’s CLI](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/#getting-started-installing-the-cli).
笔者把解压缩文件放到目录：
```shell
/Users/zgpeace/opt/spring-2.1.1.RELEASE
```
笔者Mac的命令行用的是`myzsh`，先跳转配置文件位置`.zshrc`, 编辑`.zshrc`
```shell
$ cd $home
$ vim .zshrc
```
笔者配置Java相关运行环境，记录一下
```shell
# JAVA
export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"
export MAVEN_HOME="/Users/zgpeace/opt/apache-maven-3.5.4"
export SPRING_HOME="/Users/zgpeace/opt/spring-2.1.1.RELEASE"
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$SPRING_HOME/bin:$PATH
# spring shell auto-completion
ln -s $SPRING_HOME/shell-completion/zsh/_spring /usr/local/share/zsh/site-functions/_spring
```
重新加载配置文件

```shell
$ source .zshrc
```
查看Spring版本
```shell
$ spring --version
Spring CLI v2.1.1.RELEASE
```
cd到`ThisWillActuallyRun.groovy`所在的目录, 运行命令启动应用
```shell
$ spring run ThisWillActuallyRun.groovy 
```
命令行Terminal打开新的窗口访问:
```shell
$ curl localhost:8080
Hello World!
```
**Spring Boot** 动态地在代码中增加`key annotations` ，用[Groovy Grape](http://docs.groovy-lang.org/latest/html/documentation/grape.html)去拉取类库使APP运行起来.
# 总结
恭喜你! 你成功创建了**Spring Boot**应用并加速了你的开发速度. 例子只列举了有限的服务，亲可以查看[更多在线文档](https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/).
例子代码：https://github.com/zgpeace/Spring-Boot2.1/tree/master/demo1boot

参考：
https://spring.io/guides/gs/spring-boot/
https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/
https://blog.csdn.net/forezp/article/details/70341651
