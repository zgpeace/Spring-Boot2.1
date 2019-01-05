# 易筋SpringBoot 2.1 | 第七篇：Mybatis访问MySQL
写作时间：2019-01-05 <br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA, MySQL 8.0.13
# 什么是 MyBatis ？
MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Old Java Objects,普通的 Java对象)映射成数据库中的记录。

# MySQL准备
笔者的MySQL版本为8.0.13，搭建环境可参考文章[JdbcTemplate访问MySQL](https://blog.csdn.net/zgpeace/article/details/85673765)。

# 创建数据库表

```sql
# drop table  if exists city ;

CREATE TABLE city (
    id INT PRIMARY KEY auto_increment, 
    name varchar(20),
    state varchar(20),
    country varchar(20)
);

INSERT INTO city (name, state, country) VALUES ('San Francisco', 'CA', 'US');
```

# 工程建立
工程建立的时候，需要勾选SQL的JPA和MyBatis两项：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190105112824996.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demojpa, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# 配置文件
### pom.xml 依赖配置修改，`mysql-connector-java`改为如下 ：
```xml
<dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.13</version>
      <scope>runtime</scope>
 </dependency>
```

### 数据库连接信息配置`src/main/resources/application.yml`：
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db_example
    username: springuser
    password: ThePassword
```

配置项解析请参考文章[JdbcTemplate访问MySQL](https://blog.csdn.net/zgpeace/article/details/85673765)。

# 实体类
新建实体类`com.zgpeace.demomybatis.bean.City`
```java
package com.zgpeace.demomybatis.bean;

import java.io.Serializable;

public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String state;
    private String country;

    @Override
    public String toString() {
        return String.format(
                "City[id=%d, name='%s', state='%s', country='%s']",
                id, name, state, country
        );
    }

    // getter... setter...
}
```

# DAO层接口
新建类 `com.zgpeace.demomybatis.dao.CityMapper`
```java
package com.zgpeace.demomybatis.dao;

import com.zgpeace.demomybatis.bean.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CityMapper {

    @Select("SELECT id, name, state, country FROM city WHERE state = #{state}")
    City findByState(String state);
}
```
# Application快速校验可用性
完善启动类`com.zgpeace.demomybatis.DemomybatisApplication`
```java
package com.zgpeace.demomybatis;

import com.zgpeace.demomybatis.dao.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemomybatisApplication {

    @Autowired
    private CityMapper cityMapper;

    public static void main(String[] args) {
        SpringApplication.run(DemomybatisApplication.class, args);
    }

    @Bean
    CommandLineRunner sampleCommandLineRunner() {
        return (args) -> {
            System.out.println(cityMapper.findByState("CA"));
        };
    }

}
```
控制台打印：
```shell
City[id=1, name='San Francisco', state='CA', country='US']
```
验证MyBatis用法正确，下面分层处理CRUD操作。
# DAO层CRUD
完善类`com.zgpeace.demomybatis.dao.CityMapper`
```java
package com.zgpeace.demomybatis.dao;

import com.zgpeace.demomybatis.bean.City;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CityMapper {

    @Select("SELECT id, name, state, country FROM city WHERE state = #{state}")
    City findByState(String state);

    @Select("SELECT id, name, state, country FROM city WHERE id = #{id}")
    City findById(@Param("id") int id);

    @Select("SELECT id, name, state, country FROM city")
    List<City> findCityList();

    @Insert("insert into city(name, state, country) values(#{name}, #{state}, #{country})")
    int add(@Param("name") String name, @Param("state") String state, @Param("country") String country);

    @Update("update city set name = #{name}, state = #{state}, country = #{country} where id = #{id}")
    int update(@Param("name") String name, @Param("state") String state, @Param("country") String country, @Param("id") int id);

    @Delete("delete from city where id = #{id}")
    int delete(int id);

}
```
# Service 层
新增类`com.zgpeace.demomybatis.service.CityService`
```java
package com.zgpeace.demomybatis.service;

import com.zgpeace.demomybatis.bean.City;
import com.zgpeace.demomybatis.dao.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    public City findById(int id) {
        return cityMapper.findById(id);
    }

    public List<City> findCityList() {
        return cityMapper.findCityList();
    }

    public int add(String name, String state, String country) {
        return cityMapper.add(name, state, country);
    }

    public int update(String name, String state, String country, int id) {
        return cityMapper.update(name, state, country, id);
    }

    public int delete(int id) {
        return cityMapper.delete(id);
    }
}

```

# Controller 层
新增类`com.zgpeace.demomybatis.web.CityController`
```java
package com.zgpeace.demomybatis.web;

import com.zgpeace.demomybatis.bean.City;
import com.zgpeace.demomybatis.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/list")
    public List<City> getCitys() {
        return cityService.findCityList();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable("id") int id) {
        return cityService.findById(id);
    }

    @PutMapping("/{id}")
    public String updateCity(@PathVariable("id") int id,
                             @RequestParam(value = "name", required = true) String name,
                             @RequestParam(value = "state", required = true) String state,
                             @RequestParam(value = "country", required = true) String country) {
        int count = cityService.update(name, state, country, id);
        if (count == 1) {
            return "update success";
        } else  {
            return "update fail";
        }
    }

    @PostMapping("")
    public String addCity(@RequestParam(value = "name", required = true) String name,
                          @RequestParam(value = "state", required = true) String state,
                          @RequestParam(value = "country", required = true) String country) {
        int count = cityService.add(name, state, country);
        if (count == 1) {
            return "add success";
        } else {
            return "add fail";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") int id) {
        int count = cityService.delete(id);
        if (count == 1) {
            return "delete success";
        } else {
            return "delete fail";
        }
    }

    @Transactional
    @GetMapping("/addTwoCity")
    public String addTwoCity() throws RuntimeException {
        cityService.add("HangZhou", "ZheJiang", "China");
        int temp = 1/0;
        cityService.add("WenZhou", "ZheJiang", "China");

        return "both two success";
    }
}
```
代码解析：
	`@Transactional` 表示开启事务，`addTwoCity()`方法增加两条记录为原子性操作，因为在中间有异常，所以第一条也不会入库成功。可以尝试注释掉`@Transactional`，就会有第一条入库成功。

上面代码用Postman全部测试通过，[Postman用法可点击链接](https://blog.csdn.net/zgpeace/article/details/84571205)。

# 总结
恭喜你！学会了运用Mybatis操作数据库， 开始事务`@Transactional` 原子性操作。

代码下载：
https://github.com/zgpeace/Spring-Boot2.1/tree/master/demomybatis

参考：
https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start
https://www.cnblogs.com/ityouknow/p/6037431.html
https://blog.csdn.net/forezp/article/details/70768477
http://www.mybatis.org/mybatis-3/zh/index.html

