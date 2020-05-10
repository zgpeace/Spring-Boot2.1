# Spring-Boot2.1 ~ Spring-Boot 2.2.1 学习轨迹

本系列文章环境信息:
* Spring Boot: 2.1 ~ Spring-Boot 2.2.1
* JDK: 1.8, 
* IDE: IntelliJ IDEA


| #    | 文章                                    | 说明                                      |
| :--- | :--------------------------------------- | :--------------------------------------- |
| 1    | [第一篇：构建第一个SpringBoot工程][001] |                         |
| 2    | [第二篇：Spring Boot配置文件详解][002] |                         |
| 3    | [第三篇：RestTemplate请求HTTP(1)][003] |                         |
| 4    | [第四篇：RestTemplate方法详解(2)][004] |                         |
| 5    | [第五篇：RestTemplate请求https(3)][005] |                         |
| 6    | [第六篇：JdbcTemplate访问MySQL][006] |                         |
| 7    | [第七篇：JPA访问MySQL][007] |                         |
| 8    | [第八篇：Mybatis访问MySQL][008] |                         |
| 9    | [第九篇：SpringBoot使用Redis内存数据库][009] |                         |
| 10   | [第十篇：SpringBoot使用thymeleaf入门][010] |                         |
| 11   | [第十一篇：SpringBoot使用actuator][011] |                         |
| 12   | [第十二篇：SpringBoot综合应用DataSoure][012] |                         |
| 13   | [第十三篇：SpringBoot综合应用多个DataSoure][013] |                         |
| 14   | [第十四篇：SpringBoot的HikariCP][014]  |                         |
| 15   | [第十五篇：SpringBoot连接池Druid][015]  |                         |
| 16   | [第十六篇：SpringBoot通过JDBC访问数据库][016]  |                         |
| 17   | [第十七篇：SpringBoot的事务Transaction][017]  |                         |
| 18   | [第十八篇：SpringBoot的JDBC异常][018]  |                         |
| 19   | [第十九篇：SpringBoot的常用注解][019]  |                         |
| 20   | [第廿篇：SpringBoot的复杂JPA以及源码解析][020]  |                         |
| 21   | [第廿一篇：SpringBoot的Mybatis生成工具Generator][021]  |                         |
| 22   | [第廿二篇：SpringBoot的Mybatis分页插件PageHelper][022]  |                         |
| 23   | [第廿三篇：SpringBoot之Docker安装MongoDB][023]  |                         |
| 24   | [第廿四篇：SpringBoot之MongoDB][024]  |                         |
| 25   | [第廿五篇：SpringBoot访问Docker中的Redis][025]  |                         |
| 26   | [第廿六篇：SpringBoot访问缓存抽象Cache][026]  |                         |
| 27   | [第廿七篇：SpringBoot之Redis Template和Repository][027]  |                         |
| 28   | [第廿八篇：SpringBoot之循环引用Circular Dependency][028]  |                         |
| 29   | [第廿九篇：SpringBoot之RPC入门到精通][029]  |                         |
| 30   | [第三十篇：SpringBoot Reactor响应式编程介绍][030]  |                         |
| 31   | [第三十一篇：SpringBoot Reactor响应式编程实战一][031]  |                         |
| 32   | [第三十二篇：Redis Docker入门][032]  |                         |
| 33   | [第三十三篇：SpringBoot Reactor响应式编程实战二 Redis Lettuce][033]  |                         |
| 34   | [第三十四篇：Spring Boot导出war包部署到外部Tomcat][034]  |                         |
| 35   | [第三十五篇：实战Aparche Maven 的核心概念与理论 Maven仓库管理 从入门到精通][035]  |                         |

如果文章对您有所帮助，帮忙顺手STAR.<br>
赠人玫瑰🌹，手留余香。谢谢！

[001]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/demo1boot
[002]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/democonfig
[003]: https://github.com/zgpeace/Spring-Boot2.1/blob/master/http/demoresttemplatehttp
[004]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/http/demoresttemplatemethod
[005]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/http/demoresttemplatehttps
[006]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demojdbctemplate
[007]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demojpa
[008]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demomybatis
[009]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demoredis
[010]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/tool/thymeleaf
[011]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/tool/demoactuator
[012]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demojdbccompose
[013]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demomultidatasource
[014]: https://blog.csdn.net/zgpeace/article/details/98719059
[015]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demodbdruid
[016]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demodbjdbc
[017]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demodbtransaction
[018]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demodberrorcode
[019]: https://blog.csdn.net/zgpeace/article/details/99704906
[020]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demodbjpastarbucks
[021]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/demodbmybatisgenerator
[022]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/db/DemoDBMybatisPageHelper
[023]: https://blog.csdn.net/zgpeace/article/details/100799655
[024]: https://blog.csdn.net/zgpeace/article/details/100875252
[025]: https://github.com/zgpeace/Spring-Boot2.1/tree/master/Nosql/JedisDemo
[026]: https://blog.csdn.net/zgpeace/article/details/101599443
[027]: https://blog.csdn.net/zgpeace/article/details/102130845
[028]: https://blog.csdn.net/zgpeace/article/details/102210725
[029]: https://blog.csdn.net/zgpeace/article/details/103287450
[030]: https://blog.csdn.net/zgpeace/article/details/103311708
[031]: https://blog.csdn.net/zgpeace/article/details/103321024
[032]: https://blog.csdn.net/zgpeace/article/details/101599335
[033]: https://blog.csdn.net/zgpeace/article/details/103363725
[034]: https://blog.csdn.net/zgpeace/article/details/104547552
[035]: https://blog.csdn.net/zgpeace/article/details/106039190

