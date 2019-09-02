# 易筋SpringBoot 2.1 | 第十篇：SpringBoot使用thymeleaf入门
写作时间：2019-02-01<br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA, MySQL 8.0.13
# 说明
在SpringBoot网站，HTTP请求被Controller处理后，返回一个网页。之前的例子直接返回Restful json，用注解@RestController，如果要返回网页，请用注解@Controller。在下面的例子GreetingController处理GET请求，path为/greeting，并返回一个"greeting"的页面。

# 工程建立
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demothymeleaf, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# 配置pom.xml依赖
>pom.xml添加thymeleaf依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

```
# Controller处理请求
>com.zgpeace.thymeleaf.web.GreetingController
```java
package com.zgpeace.thymeleaf.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name",
            required = false,
            defaultValue = "World") String name,
                           Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}

```
代码解析：
1. RequestParam设置请求参数，没有配置，默认值为"World"，
2. 把name属性传递给页面
3. 返回页面的名字为"greeting"，默认为.html后缀。

# 返回页面
>src/main/resources/templates/greeting.html
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<p th:text="'Hello, ' + ${name} + '!'" />
</body>
</html>

```
代码解析：
获取服务端传过来的数据：`${name}`。

启动服务器，结果测试：
浏览器输入链接：
>http://localhost:8080/greeting
>http://localhost:8080/greeting?name=zgpeace

# 总结
恭喜你，学会了应用thymeleaf访问网页。
代码下载：
https://github.com/zgpeace/Spring-Boot2.1/tree/master/thymeleaf

# 参考
https://spring.io/guides/gs/serving-web-content/
