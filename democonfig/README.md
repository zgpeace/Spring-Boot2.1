# 易筋SpringBoot2.1 | 第二篇：Spring Boot配置文件详解
写作时间：2018-12-22 <br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA,
# 配置文件说明
**Spring Boot** 配置文件允许为同一套应用，为不同的环境用不同的配置文件。比如开发环境、测试环境、生成环境。你可以用 **properties** 文件, **YAML** 文件,  **环境变量** , 和 **命令行参数** 去定制配置文件. 属性可以通过注解 **@Value** 注入内容，结构化对象可以通过注解 **@ConfigurationProperties**  注入内容。配置文件英文关键词： **Externalized Configuration** .

**Spring Boot** 配置文件加载优先级如下:
1. **Devtools**全局设置属性在路径 (~/.spring-boot-devtools.properties 当devtools激活的时候).
2. 注解 **@TestPropertySource** 在单元测试中.
3. 属性在单元测试中，用注解 **@SpringBootTest**测试应用的调用方法.
4. 命令行参数.
5. 属性配置在**SPRING_APPLICATION_JSON** (JSON 内置在环境变量或者系统属性).
6. **ServletConfig**初始化参数.
7. **ServletContext** 初始化参数.
8. **JNDI**（Java Naming and Directory Interface） 属性配置在 java:comp/env.
9. Java系统属性 ( **System.getProperties()** ).
10. OS（Operating System）环境变量.
11. **RandomValuePropertySource** 中的属性只包括在包 `random.*` .
12. [Profile-specific application properties](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties) 配置文件在packaged jar之外 (application-{profile}.properties 和 YAML ).
13. [Profile-specific application properties](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties)配置文件在packaged jar之内 (application-{profile}.properties 和 YAML).
14. 应用属性在packaged jar之外 (application.properties 和 YAML).
15. 应用属性在packaged jar之内(application.properties 和 YAML).
16. 注解 @Configuration修饰的类，里用注解**@PropertySource**  .
17. 默认属性 (配置在文件 SpringApplication.setDefaultProperties).

本章记录最常用的配置文件 **application.properties** 和 **YAML**。

# 配置文件位置读取优先级
`application.properties` 或者 `application.yml` 读取优先级如下:

1. `file:./config/`当前位置的子目录`/config` 下
2. `file:./`当前位置
3. `classpath:/config/`classpath `/config` 的包中
4. `classpath:/`classpath的根目录

# Properties文件中的占位符
Placeholders占位符，实际上是可以被后面的属性运用的变量。
通过`{占位符}`来替换掉里面对象的内容。比如下面app.description=MyApp is a Spring Boot Application
```json
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```

# 用YAML代替 Properties
[YAML](https://yaml.org/)是**JSON**的超集，可以分层来定义数据. `SpringApplication` 类自动支持 **YAML** 替换 **properties** 当`classpath`加载了类库[SnakeYAML](http://www.snakeyaml.org/).  
`spring-boot-starter`自动加载了类库**SnakeYAML**。
1. YAML分层的例子：
```yml
environments:
	dev:
		url: http://dev.example.com
		name: Developer Setup
	prod:
		url: http://another.example.com
		name: My Cool App
```
等价于properties
```yml
environments.dev.url=http://dev.example.com
environments.dev.name=Developer Setup
environments.prod.url=http://another.example.com
environments.prod.name=My Cool App
```

2. YAML配置list的例子，
```yml
my:
servers:
	- dev.example.com
	- another.example.com
```
等价于properties，数组list带了`[index]`
```json
my.servers[0]=dev.example.com
my.servers[1]=another.example.com
```

## 加载YAML
**Spring Framework** 提供了两个方便的方法加载**YAML**文件. `YamlPropertiesFactoryBean` 加载 YAML 当做 Properties，`YamlMapFactoryBean` 加载 YAML 当做a Map.

## 自定义属性
[按照教程一](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫democonfig, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。
定义属性如下：
```yml
account:
  name: zgpeace
  age: 18
  number: ${random.int}
  uuid: ${random.uuid}
  max: ${random.int(10)}
  value: ${random.value}
  greeting: hi, I'm ${my.name}
```
其中配置文件中用到了`${random}` ，它可以用来生成各种不同类型的随机值。
读取配置文件里面的内容，在属性上用注解`@Value("${属性名}")`，
新建类`com.zgpeace.democonfig.MineController.java`, 实现如下：
```java
package com.zgpeace.democonfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MineController {
    @Value("${account.name}")
    private String name;
    @Value("${account.age}")
    private int age;

    @RequestMapping(value = "/author")
    public String mine() {
        return name+ ": " + age;
    }
}
```
启动应用，命令行访问结果如下：
```shell
$ curl http://localhost:8080/author
zgpeace: 18
```

# 将配置文件的属性赋给实体类
配置文件的字段可以赋值给JavaBean, 结构化对象可以通过注解 **@ConfigurationProperties** 注入内容，创建类`com.zgpeace.democonfig.bean.ConfigBean`, 实现如下：
```java
package com.zgpeace.democonfig.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "account")
@Component
public class ConfigBean {

    private String name;
    private int age;
    private int number;
    private String uuid;
    private int max;
    private String value;
    private String greeting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
```
实现类`com.zgpeace.democonfig.MineController`修改为：
```java
package com.zgpeace.democonfig;

import com.zgpeace.democonfig.bean.ConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties({ConfigBean.class})
public class MineController {

    @Autowired
    ConfigBean configBean;

    @Value("${account.name}")
    private String name;
    @Value("${account.age}")
    private int age;

    @RequestMapping(value = "/author")
    public String mine() {
        return name+ ": " + age;
    }

    @RequestMapping(value = "/lucy")
    public String person() {
        return configBean.getGreeting() + " >>>" + configBean.getName() + " >>>" +configBean.getUuid() + " >>>" + configBean.getMax();
    }
}
```
启动应用，在命令行访问：
```shell
$ curl http://localhost:8080/lucy  
hi, I'm ${my.name} >>>zgpeace >>>86cc4c39-8715-46ef-9e9f-497a2c6fac22 >>>8
```
# 自定义配置文件
上面介绍的文件是放在`properties.yml`中，现在定义一个自己的文件
`specific.properties`, 内容如下：
```yml
com.zgpeace.hobby=run marathon
com.zgpeace.fruit=orange
```
赋值给JavaBean，需要三个注解
1. `@Configuration`
2. `@PropertySource(value = “classpath:specific.properties”)`
3. `@ConfigurationProperties(prefix = “com.zgpeace”)`
新建类`com.zgpeace.democonfig.bean.User`, 
```java
package com.zgpeace.democonfig.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:specific.properties")
@ConfigurationProperties(prefix = "com.zgpeace")
public class User {

    private String hobby;
    private String fruit;

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }
}

```
运行程序，在命令行测试：
```shell
$ curl http://localhost:8080/user
hobby: run marathon ; fruit: orange
```
# 多个环境配置文件
除了`application.properties` 或者 `application.yml`外，可以自定义环境配置文件的格式为`application-{profile}.properties`。环境有默认的定制配置文件`application-default.properties`，默认会加载。但是用户定制的文件会覆盖掉默认的文件。
比如开发环境、测试环境、生成环境定义三个配置文件
1. `application-test.properties`：测试环境
```yml
server:
  port: 8082
```
2. `application-dev.properties`：开发环境
```yml
server:
  port: 8086
```
3. `application-prod.properties`：生产环境
```yml
server:
  port: 8088
```

启用方式为设置application.yml 中设置profile: active :
```yml
spring:
  profiles:
    active: dev
```
启动应用，发现程序的端口不再是8080,而是8082。

# 代码地址
https://github.com/zgpeace/Spring-Boot2.1/tree/master/democonfig
# 参考
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html
https://blog.csdn.net/forezp/article/details/70437576

