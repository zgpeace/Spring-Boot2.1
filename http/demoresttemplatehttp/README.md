# 易筋SpringBoot 2.1 | 第三篇：RestTemplate请求HTTP(1)
写作时间：2018-12-26 <br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA,
# 说明
传统情况下在java代码里访问restful服务，一般使用Apache的HttpClient。不过此种方法使用起来太过繁琐。spring提供了一种简单便捷的模板类来进行操作，这就是RestTemplate。
![在这里插入图片描述](https://img-blog.csdnimg.cn/2018122617260984.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

使用estTemplate访问restful接口非常的简单粗暴无脑, 一句代码请求。(url, RequestMap, ResponseBean.class)这三个参数分别代表 请求地址、请求参数、HTTP响应转换被转换成的对象类型。
# 工程建立
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demoresttemplatehttp, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# 请求url
RESTful service已经准备好， [http://gturnquist-quoters.cfapps.io/api/random](http://gturnquist-quoters.cfapps.io/api/random). 这个链接随机返回SpringBoot的好处json. 格式如下：
```json
{
   type: "success",
   value: {
      id: 10,
      quote: "Really loving Spring Boot, makes stand alone Spring apps easy."
   }
}
```
RestTemplate 可以在main函数里直接调用，所以直接建一个普通的类：`com.zgpeace.demoresttemplatehttp.run.Application.java`， 
代码如下：
```java
package com.zgpeace.demoresttemplatehttp.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", String.class);
        log.info(result);
    }

}

```

打印日志内容：
```shell
{"type":"success",
 "value":{"id":1,
	"quote":"Working with Spring Boot is like pair-programming with the Spring developers."
	}
}
```
# 请求结果model化
在项目中，每次都解析字符串显得比较low，一般会对结果序列化为对象。
新建model对象：`com.zgpeace.demoresttemplatehttp.bean.Value`
```java
package com.zgpeace.demoresttemplatehttp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private Long id;
    private String quote;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
```
注解`@JsonIgnoreProperties` (来自类库 Jackson JSON)表示任何属性不在Bean定义，则忽略。
新建model对象：`com.zgpeace.demoresttemplatehttp.bean.Quote`
```java
package com.zgpeace.demoresttemplatehttp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    private String type;
    private Value value;

    public Quote() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
```

# Spring Boot通过生命周期的方式请求
完善类`com.zgpeace.demoresttemplatehttp.DemoresttemplatehttpApplication`
```java
package com.zgpeace.demoresttemplatehttp;

import com.zgpeace.demoresttemplatehttp.bean.Quote;
import com.zgpeace.demoresttemplatehttp.run.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoresttemplatehttpApplication {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(DemoresttemplatehttpApplication.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            Quote quote = restTemplate.getForObject(
                    "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            log.info(quote.toString());
        };
    }
}
```
因为Jackson JSON类库在classpath中, RestTemplate会用它(通过 [message converter](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/converter/HttpMessageConverter.html)) 把JSON 转换为对象Quote.  打印输出如下：
```shell
Quote{type='success', 
	  value=Value{id=1, 
	  			 quote='Working with Spring Boot is like pair-programming with the Spring developers.'
	  			   }
	  }
```

这里只举例RestTemplate发送HTTP GET请求. RestTemplate还只是请求方法POST, PUT, 和 DELETE.

# 总结
恭喜你！完成了RestTemplate请求http.

demo代码： https://github.com/zgpeace/Spring-Boot2.1/tree/master/demoresttemplatehttp

参考：
https://spring.io/guides/gs/consuming-rest/
https://my.oschina.net/sdlvzg/blog/1800395
