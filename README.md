English | [简体中文](./README-CN.md)

# mine-spring

A Simple Spring-Framework implementation for learning.

- api document for `mine-spring`: [mine-spring-api-document](http://mine-spring-api.yankaizhang.com/)

- Example project for `mine-spring`: [short-link-server](https://github.com/dzzhyk/short-link-server)

## Background

mine-spring is a practice product of learning Spring-Framework source code.  
Through this project, you can understand the simple implementation ways of spring and learn about Spring more clearly!

## Features

1. Simple Spring IoC container implementation.
2. AOP programming support.
3. Simple yet functional springMVC function. 
4. Almost the same annotation usage as Spring-Framework.
5. JavaConfig Class `@Configuration` support
6. MultipartFile upload and download `MultipartFile`
7. Jsp and simple HTML template support which support el-expression `${}`
8. `@RequestBody` and `@ResponseBody` support
9. `@Autowired`
10. coming soon...

## Usage

create an empty web project, then add `mine-spring` maven dependency: 

```xml
<dependency>
    <groupId>com.yankaizhang</groupId>
    <artifactId>mine-spring</artifactId>
    <version>0.0.5</version>
</dependency>
```

## Quick Example


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

For more examples, check module `mine-spring-example` and example project [short-link-server](https://github.com/dzzhyk/short-link-server)

## Related Efforts

- [spring-framework](https://github.com/spring-projects/spring-framework) - spring-projects/spring-framework

## Update

[update logs](./UPDATE.md)

## TODO

- [x] Spring IoC Container implementation
- [x] `BeanPostProcessor` and `BeanFactoryPostProcessor`
- [x] `@Configuration` and `@Bean` based Java Config support
- [x] Annotation Based AOP support `@Aspect`
- [x] `@ResponseBody` and JSON parser
- [x] `@Autowired` property injection
- [x] MultipartFile upload and download support
- [ ] `@Around` Aspect
- [ ] Simple Spring JDBC
- [ ] transaction support
- [ ] Simple ORM like Spring Data JPA

## Known Bugs

- [x] ~~The Controller's method cannot be accessed by the AOP aspect.~~
- [x] ~~Repeated registration when registering HandlerMapping.~~
- [x] ~~Circular Reference for singleton and prototype beans~~

## Maintainer

[@dzzhyk](https://github.com/dzzhyk)

## Contribute

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


If you like this repository, why not star 🌟 or fork ✈️ it ? I'm working hard 💪!

## Contact

- Author：dzzhyk
- Email：dzzhyk@qq.com
- [Personal Blog](https://dzzhyk.blog.csdn.net/)


## License

- [MIT](./LICENSE) © dzzhyk


## Inheritance relationship

- BeanDefinition

<img src="https://gitee.com/dzzhyk/MarkdownPics/raw/master/image-20210321135946207.png" alt="image-20210321135946207" style="zoom:150%;" />



- BeanFactory

<img src="https://gitee.com/dzzhyk/MarkdownPics/raw/master/image-20210321140051848.png" alt="image-20210321140051848" style="zoom:150%;" />



- ApplicationContext

<img src="https://gitee.com/dzzhyk/MarkdownPics/raw/master/image-20210321140144404.png" alt="image-20210321140144404" style="zoom:150%;" />



- AopProxy

<img src="https://gitee.com/dzzhyk/MarkdownPics/raw/master/image-20210321140304290.png" alt="image-20210321140304290" style="zoom:50%;" />

<img src="https://gitee.com/dzzhyk/MarkdownPics/raw/master/image-20210321140322045.png" alt="image-20210321140322045" style="zoom:50%;" />
