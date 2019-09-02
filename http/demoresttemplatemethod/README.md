# 易筋SpringBoot 2.1 | 第四篇：RestTemplate请求HTTP(2)
写作时间：2018-12-27<br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA,
# 说明
上一篇[SpringBoot 2.1 | 第三篇：RestTemplate请求HTTP(1)](https://blog.csdn.net/zgpeace/article/details/85265151)简单运用了RestTemplate,
本篇主要讲解RestTemplate的主要请求方法, `getForObject`, `getForEntity`, `exchange`(方法列举只用了Get)。Method包括GET、POST、PUT、DELETE。参数传递，解析等。

在讲述使用之前，想要理解SpringMVC的几个常用注解：

1. @Controller：修饰class，用来创建处理http请求的对象
2. @RestController：Spring4之后加入的注解，原来在@Controller中返回json需要@ResponseBody来配合，如果直接用@RestController替代@Controller就不需要再配置@ResponseBody，默认返回json格式。
3. @RequestMapping：配置url映射
4. @PostMapping: 这个是@RequestMapping+POST方法的简写
5. @RequestHeader: 请求Header参数
6. @PathVariable: URL路径参数，比如/user/{id}中的id参数
7. @RequestParam: URL请求参数，比如/user?id=1中的id参数
8. @RequestBody: 请求Body参数

# 工程建立
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demoresttemplatemethod, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# 创建RestTemplateConfig配置类
请求都需要用到`restTemplate`对象，用@Bean的方式注入，用同一个工厂对象统一管理`ClientHttpRequestFactory`。
新建类：
`com.zgpeace.demoresttemplatemethod.configure.RestTemplateConfig`
```java
package com.zgpeace.demoresttemplatemethod.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//ms
        factory.setConnectTimeout(15000);//ms
        return factory;
    }
}
```
# 创建Model对象User
结果实体User用于数据传递，新建类
`com.zgpeace.demoresttemplatemethod.model.User`
```java
package com.zgpeace.demoresttemplatemethod.model;

public class User {

    private Integer id;
    private String methodName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}
```
# 创建Restful被调用类
REST，即Representational State Transfer的缩写，对这个词组的翻译是表现层状态转化。

RESTful是一种软件设计风格，就是目前最流行的一种互联网软件架构。它结构清晰、符合标准、易于理解、扩展方便，所以正得到越来越多网站的采用。

这里为了演示，会将数据保存到内存Map中，实际使用肯定是保存到数据库中。

创建Restful被调用类： 
 `com.zgpeace.demoresttemplatemethod.web.UserController`
调用方法包括**GET**、**POST**、**PUT**、**DELETE**, 以及带参数的例子。
说明：**PUT**是幂等性方法，也就是请求多次跟一次的效果一样，就像微信转账一样，一次转账弱网情况下可能会重试，但是结果不会因为多次尝试而不一样。而**POST**不是幂等性的方法，也就是多次重试，会有多个结果。
```java
package com.zgpeace.demoresttemplatemethod.web;

import com.zgpeace.demoresttemplatemethod.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping(value = "/testGet", method = RequestMethod.GET)
    public User testGet() {
        User user = new User();
        user.setId(1);
        user.setMethodName("get");
        return user;
    }

    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    public User testPost() {
        User user = new User();
        user.setId(1);
        user.setMethodName("post");
        return user;
    }

    @RequestMapping(value = "/testPostParam", method = RequestMethod.POST)
    public String testPostParam(@RequestParam("id") String id, @RequestParam("methodName") String methodName) {
        System.out.println("Post id: " + id);
        System.out.println("Post methodName: " + methodName);
        return "post id{" + id + "} success";
    }

    @RequestMapping(value = "/testPut", method = RequestMethod.PUT)
    public String testPut(@RequestParam("id") String id, @RequestParam("methodName") String methodName) {
        System.out.println("put id: " + id);
        System.out.println("put methodName: " + methodName);
        return "put id{" + id + "} success";
    }

    @RequestMapping(value = "/testDel", method = RequestMethod.DELETE)
    public String testDel(@RequestParam("id") String id) {
        System.out.println("del id: " + id);
        return "del id{" + id + "} success";
    }

}

```
# 创建使用RestTemplate调用Rest接口的Controller
调用的方法包括：`getForObject`, `getForEntity`, `exchange`(方法列举只用了Get，Method包括GET、POST、PUT、DELETE).
新建类`com.zgpeace.demoresttemplatemethod.web.UserRequestController`

```java
package com.zgpeace.demoresttemplatemethod.web;

import com.zgpeace.demoresttemplatemethod.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class UserRequestController {

    @Autowired
    private RestTemplate restTemplate;

    private static String PROTOCOL = "http";
    private static String HOST = "localhost";
    private static String PORT = "8080";
    private static String PRE_URL = PROTOCOL + "://" + HOST + ":" + PORT + "/";

    private static String GET_URL = PRE_URL + "testGet";
    private static String POST_URL = PRE_URL + "testPost";
    private static String POST_PARAM_URL = PRE_URL +  "testPostParam";
    private static String PUT_URL = PRE_URL + "testPut";
    private static String DEL_URL = PRE_URL +  "testDel";

    @GetMapping("/requestTestGet")
    public String requestTestGet() throws URISyntaxException {
        // 1. getForObject()
        User user1 = restTemplate.getForObject(GET_URL, User.class);
        System.out.println("get user1: " + user1);

        // 2. getForEntity()
        ResponseEntity<User> responseEntity1 = restTemplate.getForEntity(GET_URL, User.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        HttpHeaders header = responseEntity1.getHeaders();
        User user2 = responseEntity1.getBody();
        System.out.println("get user2: " + user2);
        System.out.println("get statusCode: " + statusCode);
        System.out.println("get header: " + header);

        // 3. exchange()
        RequestEntity requestEntity = RequestEntity.get(new URI(GET_URL)).build();
        ResponseEntity<User> responseEntity2 = restTemplate.exchange(requestEntity, User.class);
        User user3 = responseEntity2.getBody();
        System.out.println("get user3: " + user3);

        return "requestTestGet";
    }

    @GetMapping("/requestTestPost")
    public String requestTestPost() throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        String data = new String();
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);

        // 1. postForObject()
        User user1 = restTemplate.postForObject(POST_URL, formEntity, User.class);
        System.out.println("post user1: " + user1);

        // 2. postForEntity()
        ResponseEntity<User> responseEntity1 = restTemplate.postForEntity(POST_URL, formEntity, User.class);
        HttpStatus statusCode = responseEntity1.getStatusCode();
        HttpHeaders header = responseEntity1.getHeaders();
        User user2 = responseEntity1.getBody();
        System.out.println("post user2: " + user2);
        System.out.println("post statusCode: " + statusCode);
        System.out.println("post header: " + header);

        // 3. exchange()
        RequestEntity requestEntity = RequestEntity.post(new URI(POST_URL)).body(formEntity);
        ResponseEntity<User> responseEntity2 = restTemplate.exchange(requestEntity, User.class);
        User user3 = responseEntity2.getBody();
        System.out.println("post user3: " + user3);

        return "requestTestPost";
    }

    @GetMapping("/requestTestPostParam")
    public String requestTestPostParam() {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id", "100");
        map.add("methodName", "requestTestPostParam");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        String data = restTemplate.postForObject(POST_PARAM_URL, request, String.class);
        System.out.println("requestTestPostParam data: " + data);
        System.out.println("requestTestPostParam success");

        return "requestTestPostParam";
    }

    @GetMapping("requestTestPut")
    public String requestTestPut() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id", "101");
        map.add("methodName", "requestTestPut");
        restTemplate.put(PUT_URL, map);
        System.out.println("requestTestPut success");

        return "requestTestPut";
    }

    @GetMapping("requestTestDel")
    public String requestTestDel() {
        HttpHeaders headers = new HttpHeaders();
        //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
       headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id", "101");
        map.add("methodName", "requestTestDel");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        // 方法一
        ResponseEntity<String> resp = restTemplate.exchange(DEL_URL , HttpMethod.DELETE, requestEntity, String.class, 227);
        System.out.println("requestTestDel response: " + resp.getBody());

        // 方法二
//        restTemplate.delete(DEL_URL + "?id={id}", 102);
        System.out.println("requestTestDel success");

        return "requestTestDel";
    }

}

```
## 方法解说：
1. HttpEntity的结构
HttpEntity是对HTTP请求的封装，包含两部分，header与body，header用于设置请求头，而body则用于设置请求体，所以其的构造器如下：
```java
//  value为请求体
//  header为请求头
HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
```
2. 后端处理前端提交的数据时，既可以使用Form解析，也可以使用JSON解析Payload字符串。

Form解析可以直接从Request对象中获取请求参数，这样对象转换与处理相对容易，但在大片JSON数据需要提交时，可能会出现大量的数据拆分与处理工作，另外针对集合类型的处理，也是其比较孱弱的地方。

而Payload的优势是一次可以提交大量JSON字符串，但无法从Request从获取参数，也会受限于JSON解析的深度（尤其是有多层对象级联的情况，最底层的对象几乎无法转换为具体类型）。
```java
 HttpHeaders headers = new HttpHeaders();
  //  form表单提交
 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
  //  payload提交
 headers.setContentType(MediaType.APPLICATION_JSON);
```
3. 用exchange方法提交
exchange可以执行所有HTTP的方法(GET、POST、PUT、DELETE、HEAD).
HttpEntity封装参数的时候必须用MultiValueMap，千万不要替换为Map与HashMap，否则参数无法传递。
```java
 //  封装参数，MultiValueMap千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id", "101");
        map.add("methodName", "requestTestDel");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
```

## 运行结果，控制台打印信息：
requestTestGet运行输出
```shell
get user1: User{id=1, methodName='get'}
get user2: User{id=1, methodName='get'}
get statusCode: 200 OK
get header: {Content-Type=[application/json;charset=UTF-8], Transfer-Encoding=[chunked], Date=[Thu, 27 Dec 2018 10:28:22 GMT]}
get user3: User{id=1, methodName='get'}
```
requestTestPost运行输出
```shell
post user1: User{id=1, methodName='post'}
post user2: User{id=1, methodName='post'}
post statusCode: 200 OK
post header: {Content-Type=[application/json;charset=UTF-8], Transfer-Encoding=[chunked], Date=[Thu, 27 Dec 2018 10:29:50 GMT]}
post user3: User{id=1, methodName='post'}
```
requestTestPostParam运行输出
```shell
PostParam id: 100
PostParam methodName: requestTestPostParam
requestTestPostParam data: post id{100} success
requestTestPostParam success
```
requestTestPut运行输出
```shell
put id: 101
put methodName: requestTestPut
requestTestPut success
```
requestTestDel运行输出
```shell
requestTestDel before
del id: 101
requestTestDel response: del id{101} success
requestTestDel success
```

# 总结
恭喜你！ 已经完成RestTemplate中的常用方法用法。

代码下载：
https://github.com/zgpeace/Spring-Boot2.1/tree/master/demoresttemplatemethod

参考：
https://my.oschina.net/sdlvzg/blog/1800395
https://blog.csdn.net/yiifaa/article/details/77939282
https://blog.csdn.net/yiifaa/article/details/73468001
https://www.xncoding.com/2017/07/05/spring/sb-restful.html
