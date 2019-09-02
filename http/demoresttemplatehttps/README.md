# 易筋SpringBoot 2.1 | 第五篇：RestTemplate请求https(3)

写作时间：2018-12-28<br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA,

# 说明
   截至2018年6月，Alexa排名前100万的网站中有34.6%使用HTTPS作为默认值[1]，互联网141387个最受欢迎网站的43.1%具有安全实施的HTTPS[2]，以及45%的页面加载（透过Firefox纪录）使用HTTPS[3]。2017年3月，中国注册域名总数的0.11％使用HTTPS。[4]

   超文本传输安全协议（英语：Hypertext Transfer Protocol Secure，缩写：HTTPS，常称为HTTP over TLS，HTTP over SSL或HTTP Secure）是一种透过计算机网络进行安全通信的传输协议。HTTPS经由HTTP进行通信，但利用SSL/TLS来加密数据包。HTTPS开发的主要目的，是提供对网站服务器的身份认证，保护交换数据的隐私与完整性。这个协议由网景公司（Netscape）在1994年首次提出，随后扩展到互联网上。
     以上内容来自维基百科。
     本文主要说明用RestTemplate请求HTTPS。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181228222330993.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

# 获取天气预报api
为了测试HTTPS，先找一个免费的天气预报资源：
1. 打开和风网站，网址是这个[https://www.heweather.com](https://www.heweather.com)，然后注册账号，找到自己的KEY，再打开这个API说明。[https://www.heweather.com/documents/api/s6/weather-forecast](https://www.heweather.com/documents/api/s6/weather-forecast)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181228223959525.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)
得出测试链接格式为：
```shell
https://free-api.heweather.net/s6/weather/forecast?location=$&key=$
```
2. 在网址[http://www.weather.com.cn/weather/101280101.shtml](http://www.weather.com.cn/weather/101280101.shtml)找到城市的天气预报，这里以广州为例，最后得出的测试链接为
```shell
https://free-api.heweather.net/s6/weather/forecast?location=CN101280101&key=34a265026b544eac9b3dcd9f104a7bb6
```
网页请求结果
```shell
{"HeWeather6":
	[{"basic": {"cid":"CN101280101","location":"广州","parent_city":"广州","admin_area":"广东","cnty":"中国","lat":"23.12517738","lon":"113.28063965","tz":"+8.00"},
	"update":{"loc":"2018-12-28 20:57","utc":"2018-12-28 12:57"},
	"status":"ok",
	"daily_forecast":[{"cond_code_d":"101","cond_code_n":"104","cond_txt_d":"多云","cond_txt_n":"阴","date":"2018-12-28","hum":"58","mr":"23:47","ms":"11:44","pcpn":"5.0","pop":"80","pres":"1024","sr":"07:07","ss":"17:50","tmp_max":"17","tmp_min":"7","uv_index":"0","vis":"10","wind_deg":"359","wind_dir":"北风","wind_sc":"4-5","wind_spd":"28"},{"cond_code_d":"104","cond_code_n":"104","cond_txt_d":"阴","cond_txt_n":"阴","date":"2018-12-29","hum":"53","mr":"00:00","ms":"12:25","pcpn":"1.0","pop":"56","pres":"1028","sr":"07:07","ss":"17:51","tmp_max":"12","tmp_min":"5","uv_index":"1","vis":"20","wind_deg":"6","wind_dir":"北风","wind_sc":"4-5","wind_spd":"27"},{"cond_code_d":"104","cond_code_n":"305","cond_txt_d":"阴","cond_txt_n":"小雨","date":"2018-12-30","hum":"41","mr":"00:45","ms":"13:04","pcpn":"5.0","pop":"80","pres":"1029","sr":"07:07","ss":"17:52","tmp_max":"11","tmp_min":"5","uv_index":"1","vis":"20","wind_deg":"0","wind_dir":"北风","wind_sc":"4-5","wind_spd":"26"}]
	}]
}
```
# 工程建立
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demoresttemplatehttps, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# pom引入httpclient依赖
```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
</dependency>
```
# 新建RestTemplate配置类
配置可以通过注解 @Configuration ，全局获取。根据最小适用原则，建立配置类`com.zgpeace.demoresttemplatehttps.config.RestTemplateConfig`
实现代码有注释掉的部分，暂时用不到，后面会说明。
```java
package com.zgpeace.demoresttemplatehttps.config;

//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.ssl.SSLContextBuilder;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // unable to find valid certification path to requested target
        /*
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
        */

        // 401 Unauthorized
        /*
        String host = "localhttps";
        int port = 23333;
        String user = "john";
        String password = "123456";
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(user, password));
        */


        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .setSSLSocketFactory(sslConnectionSocketFactory)
//                .setDefaultCredentialsProvider(credsProvider)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }
}
```
# 调用接口
完善类 `com.zgpeace.demoresttemplatehttps.DemoresttemplatehttpsApplication`

```java
package com.zgpeace.demoresttemplatehttps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoresttemplatehttpsApplication {

    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoresttemplatehttpsApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() throws Exception {
        return args -> {
            String url = "https://free-api.heweather.net/s6/weather/forecast?location=CN101280101&key=34a265026b544eac9b3dcd9f104a7bb6";
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            String body = responseEntity.getBody();
            System.out.println("body --> " + body);
        };
    }
}
```
通过 @Autowire 注入restTemplate对象。

控制台打印日志：
```shell
body --> {"HeWeather6":
	[{"basic":{"cid":"CN101280101","location":"广州","parent_city":"广州","admin_area":"广东","cnty":"中国","lat":"23.12517738","lon":"113.28063965","tz":"+8.00"},
	"update":{"loc":"2018-12-28 21:57","utc":"2018-12-28 13:57"},
	"status":"ok",
	"daily_forecast":[{"cond_code_d":"101","cond_code_n":"104","cond_txt_d":"多云","cond_txt_n":"阴","date":"2018-12-28","hum":"58","mr":"23:47","ms":"11:44","pcpn":"5.0","pop":"80","pres":"1024","sr":"07:07","ss":"17:50","tmp_max":"17","tmp_min":"7","uv_index":"0","vis":"10","wind_deg":"9","wind_dir":"北风","wind_sc":"4-5","wind_spd":"31"},{"cond_code_d":"104","cond_code_n":"104","cond_txt_d":"阴","cond_txt_n":"阴","date":"2018-12-29","hum":"53","mr":"00:00","ms":"12:25","pcpn":"1.0","pop":"56","pres":"1028","sr":"07:07","ss":"17:51","tmp_max":"12","tmp_min":"5","uv_index":"1","vis":"20","wind_deg":"359","wind_dir":"北风","wind_sc":"4-5","wind_spd":"30"},{"cond_code_d":"104","cond_code_n":"305","cond_txt_d":"阴","cond_txt_n":"小雨","date":"2018-12-30","hum":"41","mr":"00:45","ms":"13:04","pcpn":"5.0","pop":"80","pres":"1029","sr":"07:07","ss":"17:52","tmp_max":"11","tmp_min":"5","uv_index":"1","vis":"20","wind_deg":"9","wind_dir":"北风","wind_sc":"4-5","wind_spd":"26"}]
	}]
}
```

# 证书无效的网站
证书无效的网站，请求会报错`unable to find valid certification path to requested target` ，解决方案如下：
```java
// unable to find valid certification path to requested target
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);

	CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setSSLSocketFactory(sslConnectionSocketFactory)
//                .setDefaultCredentialsProvider(credsProvider)
                .build();
```

# 需要授权登录的网站
需要授权登录的网站，需要用户名密码，如下图演示：
![](https://o7planning.org/en/11647/cache/images/i/14314487.gif)
解决方案如下：
```java
// 401 Unauthorized
        String host = "localhttps";
        int port = 23333;
        String user = "john";
        String password = "123456";
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host, port),
                new UsernamePasswordCredentials(user, password));

        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setDefaultCredentialsProvider(credsProvider)
                .build();
```



# 总结
恭喜你！学会了RestTemplate请求https。
代码下载：https://github.com/zgpeace/Spring-Boot2.1/tree/master/demoresttemplatehttps

参考：
http://www.rain1024.com/2017/04/26/api-article76/
http://www.tutorialsteacher.com/https/what-is-https
https://developers.google.com/web/fundamentals/security/encrypt-in-transit/why-https
https://zh.wikipedia.org/wiki/%E8%B6%85%E6%96%87%E6%9C%AC%E4%BC%A0%E8%BE%93%E5%AE%89%E5%85%A8%E5%8D%8F%E8%AE%AE
https://o7planning.org/en/11649/secure-spring-boot-restful-service-using-basic-authentication
