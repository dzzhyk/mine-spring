# mine-springframework
> A Simple Spring-Framework implementation for learning.

[README_zh_CN](./README_zh_CN.md)

## Background

mine-springframework is a practice product of learning Spring-Framework source code.  
Through this project, you can understand the simple implementation ways of spring and learn about Spring more clearly!

## Features

1. Simple Spring IoC container implementation.
2. Simple yet functional springMVC function.
3. Annotation based AOP programming support.
4. Almost the same annotation usage as Spring-Framework.
5. @Configuration and @Bean to register beans.
6. coming soon...

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

Use [maven coordinates](https://en.wikipedia.org/wiki/Apache_Maven) to import the following dependencies:

```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>dzzhyk-springframework-${module}</artifactId>
  <version>${version}</version>
</dependency>
```

### 3. Create your project through a maven-archetype.
Directly use the following provided maven-archetype to quickly create a web project using mine-springframework.  

This requires that you need to add this archetype to your maven tool.

```xml
<dependency>
  <groupId>com.yankaizhang</groupId>
  <artifactId>dzzhyk-archetype-springframework</artifactId>
  <version>${version}</version>
</dependency>
```

## Related Efforts

- [spring-framework](https://github.com/spring-projects/spring-framework) - spring-projects/spring-framework

## Update

[logs](./UPDATE.md)

## TODO

- [ ] Simple Spring JDBC.
- [x] Multi-Aspect AOP support.
- [ ] Simple ORM like Spring Data JPA.
- [ ] @ResponseBody and JSON parser.
- [ ] Simplify to Controller params.
- [x] @Configuration ConfigClass support

## Known Bugs

**NEED YOUR HELP!**

- [x] The Controller's method cannot be accessed by the AOP aspect.


## Maintainer

[@dzzhyk.](https://github.com/dzzhyk)

## Contribute

Feel free to dive in! Open an issue or submit PRs.


## Contact

- Author：dzzhyk
- Email：dzzhyk@qq.com
- [Personal Blog](https://dzzhyk.blog.csdn.net/)


## License

- [MIT](./LICENSE) © dzzhyk