[English](./README.md) | 简体中文

# mine-spring

一个用于学习的简洁Spring框架实现

## 背景

mine-spring是本人学习spring框架原理和学习spring源码的练习产物，免费分享给大家！
通过本项目，可以了解Spring的主要模块如IoC、AOP、webmvc、JDBC的实现原理，同时在学习的过程中会进一步提升阅读Spring源码的能力。    
生命不息，折腾不止...

## 现有功能

1. spring IoC 容器实现
2. AOP 面向切面编程支持
3. 简单又不失功能的springMVC功能
4. 使用Java注解配置类 `@Configuration` 配置Bean对象
5. 几乎和Spring相同的注解用法
6. 文件上传和文件下载支持
7. 支持Jsp模板、自建了一个内置HTML解析模板
8. `@RequestBody`和`@ResponseBody`请求体和响应体自动解析
9. 自动装配`@Autowired`功能
10. 还有更多...

## 使用mine-spring

创建一个web项目，然后添加如下`mine-spring`的jar依赖即可：

```xml
<dependency>
    <groupId>com.yankaizhang</groupId>
    <artifactId>mine-spring</artifactId>
    <version>0.0.4</version>
</dependency>
```

导入完成后就可以使用`mine-spring`的相关功能了


## 示例

```java
@Controller
public class TestController {

    @Autowired
    TestServiceImpl testService;

    @RequestMapping("/index")
    public String index(){
        testService.hi();
        return "index";
    }
}
```
更多详细的示例直接查看 `mine-spring-example` 模块

## 相关项目

- [spring-framework](https://github.com/spring-projects/spring-framework) - Spring 框架源码

## 更新日志
[点这里查看更新日志](./UPDATE.md)

## TODO

- [x] Spring IoC 容器实现
- [x] `BeanPostProcessor` 与 `BeanFactoryPostProcessor` bean生命周期处理器实现
- [x] `@Configuration` 和 `@Bean` 支持的Java配置类解析功能
- [x] 基于注解配置的AOP面向切面编程 `@Aspect`
- [x] `@RequestBody`和`@ResponseBody`请求体和响应体自动解析
- [x] `@Autowired` 自动属性注入
- [x] 文件上传下载支持
- [ ] `@Around` 环绕通知
- [ ] 简易 Spring JDBC 实现
- [ ] 事务支持
- [ ] 简易 ORM 框架实现

## 已知问题

- [x] ~~Controller方法不能被AOP切面切入问题~~
- [x] ~~注解HandlerMapping的时候重复注册的问题~~

## 持有者

[@dzzhyk](https://github.com/dzzhyk)

## 贡献与合作

<a href="https://github.com/dzzhyk">
    <img class="d-block avatar-user" src="https://avatars0.githubusercontent.com/u/36625372?s=64&amp;v=4" width="32" height="32" alt="@dzzhyk">
</a>
<a href="https://github.com/Amber-coder">
      <img class="d-block avatar-user" src="https://avatars0.githubusercontent.com/u/54784449?s=64&amp;v=4" width="32" height="32" alt="@Amber-coder">
</a>
<a href="https://github.com/adiaoer">
      <img class="d-block avatar-user" src="https://avatars1.githubusercontent.com/u/30997087?s=64&amp;v=4" width="32" height="32" alt="@adiaoer">
</a>
<a href="https://github.com/daydreamofscience">
      <img class="d-block avatar-user" src="https://avatars3.githubusercontent.com/u/73294057?s=64&amp;v=4" width="32" height="32" alt="@daydreamofscience">
</a>

如果您感觉不错，还请收藏 🌟 或者fork ✈️ 这个项目，这是我持续更新的动力 💪！

## 联系方式

- 作者：dzzhyk
- 邮箱：dzzhyk@qq.com
- [欢迎关注CSDN](https://dzzhyk.blog.csdn.net/)

## 开源协议

- [MIT](./LICENSE) © dzzhyk