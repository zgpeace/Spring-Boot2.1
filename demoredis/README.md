写作时间：2019-01-29
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA, MySQL 8.0.13
# Redis 介绍
Redis是目前业界使用最广泛的内存数据存储。相比memcached，Redis支持更丰富的数据结构，例如hashes, lists, sets等，同时支持数据持久化。除此之外，Redis还提供一些类数据库的特性，比如事务，HA，主从库。可以说Redis兼具了缓存系统和数据库的一些特性，因此有着丰富的应用场景。

# 安装 Redis Server环境
1. 安装Redis
```shell
brew install redis

```
2. 启动Redis服务器：
```shell
redis-server

```
启动成功日志：
```shell
20068:C 29 Jan 2019 09:35:17.742 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
20068:C 29 Jan 2019 09:35:17.743 # Redis version=5.0.3, bits=64, commit=00000000, modified=0, pid=20068, just started
20068:C 29 Jan 2019 09:35:17.743 # Warning: no config file specified, using the default config. In order to specify a config file use redis-server /path/to/redis.conf
20068:M 29 Jan 2019 09:35:17.744 * Increased maximum number of open files to 10032 (it was originally set to 256).
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 5.0.3 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6379
 |    `-._   `._    /     _.-'    |     PID: 20068
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

20068:M 29 Jan 2019 09:35:17.747 # Server initialized
20068:M 29 Jan 2019 09:35:17.747 * DB loaded from disk: 0.000 seconds
20068:M 29 Jan 2019 09:35:17.747 * Ready to accept connections

```
3.  启动Redis客户端
```shell
redis-cli

```
> 操作命令都在客户端显示

4. 操作字符串
```shell
127.0.0.1:6379> set name zgpeace
OK
127.0.0.1:6379> get name
"zgpeace"

```
5. Hashes 哈希值
```shell
127.0.0.1:6379> hmset student username zgpeace password ThePassword age 18
OK
127.0.0.1:6379> hgetall student
1) "username"
2) "zgpeace"
3) "password"
4) "ThePassword"
5) "age"
6) "18"
```
6. Lists 列表
```shell
127.0.0.1:6379> lpush classmates JackMa
(integer) 1
127.0.0.1:6379> lpush classmates Lucy
(integer) 2
127.0.0.1:6379> lrange classmates 0 10
1) "Lucy"
2) "JackMa"

```
7. Redis有序集合
```shell
127.0.0.1:6379> zadd db 1 redis
(integer) 1
127.0.0.1:6379> zadd db 2 mongodb
(integer) 1
127.0.0.1:6379> zadd db 3 mysql
(integer) 1
127.0.0.1:6379> zadd db 4 oracle
(integer) 1
127.0.0.1:6379> zrange db 0 10 withscores
1) "redis"
2) "1"
3) "mongodb"
4) "2"
5) "mysql"
6) "3"
7) "oracle"
8) "4"

```
8. Redis发布订阅
启动客户端一```redis-cli```
监听两个channel，wechat和messages
```shell
subscribe wechat messages
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "wechat"
3) (integer) 1
1) "subscribe"
2) "messages"
3) (integer) 2

```

开启新的控制台，启动客户端二```redis-cli```， 并发布消息
```shell
127.0.0.1:6379> publish wechat "Hello"
(integer) 1

```
客户端一收到消息
```shell
1) "message"
2) "wechat"
3) "Hello"

```
9. 更多命令参考[Redis官网](https://redis.io/commands)

# 工程建立
工程建立的时候，需要勾选NOSQL的Redis：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190129101053415.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demoredis, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# 配置文件
### 数据库连接信息配置`src/main/resources/application.yml`：
```yml
spring:
  redis:
    host: localhost #服务器地址
    port: 6379      #服务器连接端口
    database: 1     #数据库索引(默认为0)
    password:       #服务器连接密码(默认为空)
```
# 测试访问
通过编写测试用例，设置Redis的key, value, 读取验证。
修改测试类`com.zgpeace.demoredis.DemoredisApplicationTests` 
```java
package com.zgpeace.demoredis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoredisApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws Exception {
        String key = "yourState";
        String value = "passion";
        stringRedisTemplate.opsForValue().set(key, value);
        Assert.assertEquals("passion", stringRedisTemplate.opsForValue().get(key));
    }

    @Test
    public void contextLoads() {
    }

}
```

### 注意必须启动redis-server，否则连接失败，
Terminal启动redis-server
```shell
% redis-server
```
在方法左侧，点击单元测试按钮，验证通过。

关掉redis-server的方法，先找到正在运行的pid
```shell
$ ps aux | grep redis
MyUser  8821   0.0  0.0  2459704    596   ??  S    4:54PM   0:03.40 redis-server *:6379

```
或者用 `ps -ef|grep redis`
手动kill掉pid
```shell
$ kill -9 8821
```

# 总结
恭喜你！学会了操作Redis。
代码地址：https://github.com/zgpeace/Spring-Boot2.1/tree/master/demoredis

# 参考

https://blog.csdn.net/forezp/article/details/70991675
https://blog.csdn.net/forezp/article/details/61471712
https://spring.io/guides/gs/messaging-redis/
https://www.cnblogs.com/ityouknow/p/5748830.html
http://blog.didispace.com/springbootredis/
