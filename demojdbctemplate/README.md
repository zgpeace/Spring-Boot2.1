# 易筋SpringBoot 2.1 | 第六篇：JdbcTemplate访问MySQL
写作时间：2019-01-03<br>
Spring Boot: 2.1 ,JDK: 1.8, IDE: IntelliJ IDEA, MySQL 8.0.13
# 说明
数据需要持久化保存，无论是客户端的SQLLITE，还是服务端的MySQL。这里记录最原始的访问数据库方式，JdbcTemplate访问MySQL. Spring 提供了一个模板类JdbcTemplate，能够容易跟数据库和数据库连接JDBC(Java DataBase Connectivity)打交道。 绝大多数的JDBC代码在写业务代码之前需要很多准备工作：获取数据库资源，连接管理，异常处理，常规错误校验. JdbcTemplate帮你处理上面的所有问题， 让开发者专注于业务. 

# MySQL数据库
1. 首先需要到[MySQL的官网](https://dev.mysql.com/downloads/)下载社区版的安装文件。笔者下载的是最新版本8.0.13.
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190103082646128.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

2. 接下来：下载 > 安装 > 设置root密码
安装完成以后，安装路径在`/usr/local`，笔者的MySQL路径为：`/usr/local/mysql-8.0.13-macos10.14-x86_64`。
3. 在命令行输入mysql命令：
```shell
$ mysql
zsh: command not found: mysql
```
4. 发现找不到命令，需要先设置环境变量，笔者的命令行环境为myzsh，设置如下：
```shell
$ cd $home
$ vim .zshrc
```

5. 在最下面加上环境变量配置
```shell
# Mysql
export MYSQL_HOME="/usr/local/mysql-8.0.13-macos10.14-x86_64"
export PATH=$MYSQL_HOME/bin:$PATH
```
6. 保存esc >  `:wq`, 重新加载配置文件
```shell
$ source .zshrc
```
7. 连接数据库：
```shell
$ mysql -uroot -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 24
Server version: 8.0.13 MySQL Community Server - GPL
```
8. 新建数据库和用户(注意： `--` 后面是注释)
```shell
mysql> create database db_example; -- Create the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_example.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database
```
9. 显示数据库：
```shell
mysql> show databases
    -> ;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| sys                |
| db_example         |
+--------------------+
5 rows in set (0.00 sec)
```
10. 选择数据库
```shell
mysql> use mysql;
Database changed
```
11. 显示数据库中的表
```shell
mysql> show tables;
+---------------------------+
| Tables_in_mysql           |
+---------------------------+
| columns_priv              |
| component                 |
...
```
# MySQL客户端
在命令行里面写sql不方便，可以下载客户端[MySQL Workbench 8.0.13](https://dev.mysql.com/downloads/workbench/)，查询例子如下，执行快捷键`cmd+enter`
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190103112340788.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)

忘记root密码也可以修改，建议保存好密码。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190103112709278.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3pncGVhY2U=,size_16,color_FFFFFF,t_70)
# 工程建立
参照教程[【SpringBoot 2.1 | 第一篇：构建第一个SpringBoot工程】](https://blog.csdn.net/zgpeace/article/details/85111272)新建一个Spring Boot项目，名字叫demojdbctemplate, 在目录`src/main/java/resources` 下找到配置文件`application.properties`，重命名为`application.yml`。

# 配置文件
### pom.xml 依赖配置添加：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.13</version>
 </dependency>
```

MySQL连接的配置`mysql-connector-java`信息来自官网 »
1.  [8.0.13 链接](https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.13)
2. [所有版本链接](https://mvnrepository.com/artifact/mysql/mysql-connector-java)

### 数据库连接信息配置`src/main/resources/application.yml`：
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db_example
    username: springuser
    password: ThePassword
```
# 代码实现
1. 新建实体类`com.zgpeace.demojdbctemplate.bean.Customer`
```java
package com.zgpeace.demojdbctemplate.bean;

public class Customer {
    private long id;
    private String firstName, lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    // getters & setters omitted for brevity

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

```

2. 完善启动类`com.zgpeace.demojdbctemplate.DemojdbctemplateApplication`
```java
package com.zgpeace.demojdbctemplate;

import com.zgpeace.demojdbctemplate.bean.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemojdbctemplateApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemojdbctemplateApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemojdbctemplateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");

        jdbcTemplate.query("SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh"},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
                        rs.getString("last_name"))).forEach(customer -> log.info(customer.toString()));


    }
}
```

3. 运行工程, 控制台打印信息如下：
```shell
2019-01-03 10:51:35.301  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Creating tables
2019-01-03 10:51:35.303  INFO 23276 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2019-01-03 10:51:35.626  INFO 23276 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2019-01-03 10:51:35.723  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Inserting customer record for John Woo
2019-01-03 10:51:35.723  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Inserting customer record for Jeff Dean
2019-01-03 10:51:35.723  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Inserting customer record for Josh Bloch
2019-01-03 10:51:35.723  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Inserting customer record for Josh Long
2019-01-03 10:51:35.743  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Querying for customer records where first_name = 'Josh':
2019-01-03 10:51:35.755  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Customer[id=3, firstName='Josh', lastName='Bloch']
2019-01-03 10:51:35.755  INFO 23276 --- [           main] c.z.d.DemojdbctemplateApplication        : Customer[id=4, firstName='Josh', lastName='Long']
```

# 总结
恭喜你！实现了JdbcTemplate访问MySQL。 企业项目一般会用Mybatis或者JPA操作数据库，所以这里只要会用JdbcTemplate就好。

代码下载：
https://github.com/zgpeace/Spring-Boot2.1/tree/master/demojdbctemplate

参考：
https://spring.io/guides/gs/relational-data-access/
https://spring.io/guides/gs/accessing-data-mysql/
https://blog.csdn.net/forezp/article/details/70477821
https://www.jianshu.com/p/a8e4068a7a8a
