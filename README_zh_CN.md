# mine-springframework

> 简洁的Spring框架实现

[Chinese](./README_zh_CN.md)

## 背景

mine-springframework是本人学习spring框架原理和学习spring源码的练习产物。  
通过本项目，可以了解spring的简易原理，以及spring主要功能的底层实现，学习spring更加清晰明白！

## 现有功能

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

## 如何直接使用mine-springframework

### 1. 在项目中添加lib依赖

1. 下载本项目的release版本jar包
2. 使用IDE（例如IDEA）创建一个web项目
3. 在项目目录下新建lib目录，将本项目jar包放置于lib目录下，然后添加该lib目录为External Library

### 2. 用Spring项目的方式创建项目
使用方式基本和创建一个Spring项目完全相同，只需要创建一个Spring-web项目并且导入maven依赖即可

首先你需要在maven的setting.xml中添加我的Nexus仓库，这样就可以使用到本项目的发布jar包了

```xml
<mirrors>
<mirror>
  <id>dzzhyk-nexus</id>
  <mirrorOf>*</mirrorOf>
  <name>dzzhyk-nexus</name>
  <url>http://maven.yankaizhang.com/repository/public/</url>
</mirror>
</mirrors>
```
这里覆盖了所有仓库，你可以根据你自己调整，当然直接使用也是没关系的，我的私有主仓库也代理了aliyun国内仓库，只是受限于云服务器下载限制

所以接下来需要使用maven坐标导入spring-core和本项目的jar包：

```xml
<dependencies>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-core</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-beans</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-context</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-aop</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-webmvc</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```


### 3. 用maven-archetype方式创建项目 (还不完善，暂时不要用)
或者直接使用以下提供好的maven archetype工程文件来快速创建一个使用了mine-springframework的web工程

这要求你首先需要添加这个archetype到你的maven工具

```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>dzzhyk-archetype-springframework</artifactId>
  <version>${version}</version>
</dependency>
```

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
- [ ] Controller参数简化
- [x] @Configuration配置类和@Bean注解配置
- [x] 文件上传和文件下载支持
- [ ] 更加完整正规的AnnotationConfigClassApplicationContext容器
- [ ] beanPostProcessor 和 beanFactoryPostProcessor实现，以及其容器实现

## 已知问题
~~太惨了~~
- [x] ~~Controller方法不能被AOP切面切入问题~~

## 持有者

[@dzzhyk.](https://github.com/dzzhyk)

## 贡献与合作

欢迎任何形式的issue和PR，需要你的帮助！

## 联系方式

- 作者：dzzhyk
- 邮箱：dzzhyk@qq.com
- [欢迎关注CSDN](https://dzzhyk.blog.csdn.net/)

## 开源协议

- [MIT](./LICENSE) © dzzhyk