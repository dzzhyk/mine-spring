English | [简体中文](./README-CN.md)

# mine-spring
A Simple Spring-Framework implementation for learning.

Check project wiki => [mine-spring-wiki](https://github.com/dzzhyk/mine-spring/wiki)

## Background

mine-spring is a practice product of learning Spring-Framework source code.  
Through this project, you can understand the simple implementation ways of spring and learn about Spring more clearly!

## Features

1. Simple Spring IoC container implementation.
2. Simple yet functional springMVC function.
3. Annotation based AOP programming support.
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
    <version>0.0.2</version>
</dependency>
```

## Quick Example


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

For more examples, check module `mine-spring-example`

## Related Efforts

- [spring-framework](https://github.com/spring-projects/spring-framework) - spring-projects/spring-framework

## Update

[update logs](./UPDATE.md)

## TODO

- [ ] Simple Spring JDBC.
- [x] Multi-Aspect AOP support.
- [ ] Simple ORM like Spring Data JPA.
- [x] @ResponseBody and JSON parser.
- [x] Simplify to Controller params.
- [x] @Configuration based Java ConfigClass support
- [x] MultipartFile upload and download
- [x] A tiny `AnnotationConfigClassApplicationContext` impl
- [ ] `BeanPostProcessor` and `BeanFactoryPostProcessor`
- [ ] transaction support
- [ ] @Around Aop

## Known Bugs

- [x] ~~The Controller's method cannot be accessed by the AOP aspect.~~
- [x] ~~Repeated registration when registering HandlerMapping.~~

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