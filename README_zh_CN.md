# mine-springframework

> 一个用于学习的简洁Spring框架实现

[English](./README.md)

## 背景

mine-springframework是本人学习spring框架原理和学习spring源码的练习产物，免费分享给大家！
通过本项目，可以了解Spring的主要模块如IoC、AOP、webmvc、JDBC的实现原理，同时在学习的过程中会进一步提升阅读Spring源码的能力。  
本项目正在持续更新中...  
生命不息，折腾不止...

## 现有功能

> "To use this project like using Spring-Framework"

```java
@Controller
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/index")
    public String index(){
        testService.hi();
        return "index";
    }
}
```

更多详细的示例直接查看 `project-test` 模块

1. springIoC容器实现
2. 简单又不失功能的springMVC功能
3. AOP编程
4. 几乎和Spring相同的注解用法
5. 支持使用Java注解配置类配置Bean对象
6. 文件上传和文件下载支持
7. 还有更多...

## 下载源码

```shell script
$ git clone https://github.com/dzzhyk/mine-springframework.git
$ cd mine-springframework
```

## 使用mine-springframework构建应用

### 1. 在项目中添加lib依赖

1. 下载本项目的release版本jar包
2. 使用IDE（例如IDEA）创建一个web项目
3. 在项目目录下新建lib目录，将本项目jar包放置于lib目录下，然后添加该lib目录为External Library

### 2. 用Spring项目的方式创建项目

使用方式基本和创建一个Spring项目完全相同，只需要创建一个Spring-web项目并且导入maven依赖即可

首先你需要在pom.xml中添加我的Nexus仓库，这样就可以使用到本项目的发布的所有jar包了

```xml
<repositories>
    <!-- https://github.com/dzzhyk/mine-springframework -->
    <repository>
        <id>dzzhyk-nexus</id>
        <name>dzzhyk-nexus</name>
        <url>http://maven.yankaizhang.com/repository/github/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

所以接下来只需要使用maven坐标导入一个jar包即可：
`dzzhyk-springframework-pom`会帮你引入当前项目的所有依赖

```xml
<dependencies>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-pom</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```


### 3. 用maven-archetype方式创建项目 (推荐)

我为了更方便大家体验到本项目的成果，特意为大家制作了一个基于本项目的maven骨架模板`mine-springframework-archetype`

大家使用这个maven骨架工程可以快速上手使用本项目

这要求你首先需要添加我的archetype到你的`maven`工具：

```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>mine-springframework-archetype</artifactId>
  <version>RELEASE</version>
</dependency>
```

紧接着在你的`IDE`中选择从maven archetype创建即可

## 相关项目

- [spring-framework](https://github.com/spring-projects/spring-framework) - Spring 框架源码

## 更新日志
[logs](./UPDATE.md)

## TODO
fork and do it yourself!

- [ ] springJDBC实现
- [x] 多切面AOP支持
- [ ] 类似JPA的简易orm框架实现
- [ ] @ResponseBody注解和json解析支持
- [x] Controller参数简化
- [x] @Configuration配置类和@Bean注解配置
- [x] 文件上传和文件下载支持
- [ ] 更加完整正规的AnnotationConfigClassApplicationContext容器
- [ ] beanPostProcessor 和 beanFactoryPostProcessor实现，以及其容器实现
- [ ] 事务支持
- [ ] @Around Aop环切实现

## 已知问题
~~太惨了~~
- [x] ~~Controller方法不能被AOP切面切入问题~~
- [ ] 注解HandlerMapping的时候重复注册的问题

## 持有者

[@dzzhyk](https://github.com/dzzhyk)

## 贡献与合作

欢迎任何形式的issue和PR，需要你的帮助！

## 联系方式

- 作者：dzzhyk
- 邮箱：dzzhyk@qq.com
- [欢迎关注CSDN](https://dzzhyk.blog.csdn.net/)

## 开源协议

- [MIT](./LICENSE) © dzzhyk