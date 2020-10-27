# mine-springframework 简洁的Spring框架实现

## 背景

手写springframework是本人学习spring框架原理和学习spring源码的练习产物。  
通过本项目，可以了解spring的简易原理，以及spring主要功能的底层实现，学习spring更加清晰明白！

## 功能

1. springIoC容器实现
2. 简单又不失功能的springMVC功能
3. AOP编程
4. 以上功能的注解支持，注解名称和使用方法基本与spring相同~

## 下载源码

## 如何使用mine-springframework
使用方式基本和创建spring项目完全相同，只需要创建一个web项目并且导入maven依赖即可

首先可以下载项目源码，然后使用maven进行打包得到jar，在项目中添加jar包依赖即可使用

---

除此之外，如果你可以访问我的maven仓库，可以直接使用maven坐标导入依赖：
```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>dzzhyk-springframework</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

或者使用以下提供好的maven archetype工程文件来快速创建工程

```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>dzzhyk-archetype-springframework</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 相关项目

- [spring-framework](https://github.com/spring-projects/spring-framework) - Spring 框架源码

## 更新日志
[logs](./UPDATE.md)

## TODO

- [ ] springJDBC实现
- [x] 多切面AOP支持
- [ ] 类似JPA的简易orm框架实现
- [ ] @ResponseBody注解和json解析支持
- [ ] Controller参数简化

## 已知问题

- [ ] Controller方法不能被AOP切面切入问题


## 联系作者

- 作者：dzzhyk
- 邮箱：1354839386@qq.com
- [欢迎关注CSDN](https://dzzhyk.blog.csdn.net/)

## 开源协议

- [MIT]() © Richard Littauer