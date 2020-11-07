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

First, you need to add my Nexus Repository in your maven's `setting.xml`, so that you can use the snapshot-jar of this project.

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
All the Repository are override here. You can adjust it according to your own. Of course, it doesn’t matter if you use it directly. 
My private Nexus Repository also acts as an proxy for the central Repository, but it is limited by the download limit of the server.

Next, you need to use maven coordinates to import dependencies of this project:

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


### 3. Create your project through a maven-archetype. (NOTE: NOT available NOW)
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
fork and do it yourself!

- [ ] Simple Spring JDBC.
- [x] Multi-Aspect AOP support.
- [ ] Simple ORM like Spring Data JPA.
- [ ] @ResponseBody and JSON parser.
- [ ] Simplify to Controller params.
- [x] @Configuration based Java ConfigClass support
- [x] MultipartFile upload and download
- [ ] a tiny AnnotationConfigClassApplicationContext impl
- [ ] beanPostProcessor and beanFactoryPostProcessor

## Known Bugs

**NEED YOUR HELP!**

- [x] The ~~Controller's method cannot be accessed by the AOP aspect.~~


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