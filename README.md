# mine-springframework
> A Simple Spring-Framework implementation for learning.

[中文README](./README_zh_CN.md)

## Background

mine-springframework is a practice product of learning Spring-Framework source code.  
Through this project, you can understand the simple implementation ways of spring and learn about Spring more clearly!

## Features

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

For usage-examples, check module `project-example`

1. Simple Spring IoC container implementation.
2. Simple yet functional springMVC function.
3. Annotation based AOP programming support.
4. Almost the same annotation usage as Spring-Framework.
5. JavaConfig Class support
6. MultipartFile upload and download
7. coming soon...


## Install

```shell script
$ git clone https://github.com/dzzhyk/mine-springframework.git
$ cd mine-springframework
```

## Usage

### 1. Add lib dependency to your project.

1. Download the latest release version .jar package of this project.
2. Create or open your web project using an IDE (such as `IntelliJ IDEA`).
3. Create a new `lib` directory under your project directory, place the jar package in it,
 and then add `lib` as External Library for your project.

### 2. Create a new project in the way of a classical Spring project.
The usage is basically the same as creating a Spring project, you only need to create a Spring-web project and import maven dependencies.  

First, you need to add my Nexus Repository in your `pom.xml`, so that you can use the released or snapshot jar of this project.

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

Next, you need to use maven coordinates to import all dependencies you need of this project with specified version.
just add `dzzhyk-springframework-pom`, and it will get everything you need.

```xml
<dependencies>
    <dependency>
        <groupId>com.yankaizhang</groupId>
        <artifactId>dzzhyk-springframework-pom</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 3. Create your project through a maven-archetype. (Recommended)

In order to make it easier for everyone to try this project, 

I made a maven archetype `mine-springframework-archetype` based on this project for you.

Everyone can use this maven-archetype to quickly get started with this project.

This requires that you first need to add this archetype to your `maven` tool:

```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>mine-springframework-archetype</artifactId>
  <version>RELEASE</version>
</dependency>
```

Then create a new project by selecting `create from maven-archetype` in your `IDE`

## Related Efforts

- [spring-framework](https://github.com/spring-projects/spring-framework) - spring-projects/spring-framework

## Update

[logs](./UPDATE.md)

## TODO
fork and do it yourself!

- [ ] Simple Spring JDBC.
- [x] Multi-Aspect AOP support.
- [ ] Simple ORM like Spring Data JPA.
- [ ] @ResponseBody and JSON parser.
- [x] Simplify to Controller params.
- [x] @Configuration based Java ConfigClass support
- [x] MultipartFile upload and download
- [ ] a tiny AnnotationConfigClassApplicationContext impl
- [ ] beanPostProcessor and beanFactoryPostProcessor
- [ ] transaction support
- [ ] @Around Aop

## Known Bugs

**NEED YOUR HELP!**

- [x] The ~~Controller's method cannot be accessed by the AOP aspect.~~
- [x] Repeated registration when registering HandlerMapping.

## Maintainer

[@dzzhyk](https://github.com/dzzhyk)

## Contribute

Feel free to dive in! Open an issue or submit PRs.

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

## Contact

- Author：dzzhyk
- Email：dzzhyk@qq.com
- [Personal Blog](https://dzzhyk.blog.csdn.net/)


## License

- [MIT](./LICENSE) © dzzhyk