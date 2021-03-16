# Release

---

- 0.0.3

1. 在这个版本，mine-spring的IoC容器实现更加完整，接口设计、功能分配、继承与实现关系更加合理
2. 合理的容器实现为功能扩展带来了便利，现在允许使用`BeanPostProcessor` 与 `BeanFactoryPostProcessor`两类处理器及其进一步实现接口对bean生命周期进行控制
3. 处理器的实现使得bean生命周期管理更加方便，现在配置类、AOP均通过处理器的方式给容器赋予新的功能
4. 新增了一些注解：`@Import`、`@Qualifier`、`@Lazy`、`@EnableAspectJAutoProxy`等
5. 功能上对于前面版本的一些内容做了取舍，比如：现在起不能使用接口类对象接受`@Autowired`注解的自动注入，当然，这可能会带来一些不便
    ```java
       @Autowired
       TestServiceImpl service;    // correct
   
       @Autowired
       TestService service;        // wrong
    ```
6. 使用容器的getBean方法获取对象不需要接受异常了，因为实在是太麻烦了
7. 请注意：如果要编译运行mine-spring项目，JDK版本 >= 8 并且开启javac参数 `-parameters`，否则可能导致请求参数解析为null的情况
    > mine-spring使用了Java反射机制在运行时动态获取类对象，从而获取方法信息  
     JDK8 之前通过反射获取方法的参数，名称均为arg0 arg1...等，这会导致请求参数内容无法填充进方法参数  
     JDK8 之后加入了 -parameters 编译参数来指定编译字节码保存方法参数信息，从而才能通过反射获取到方法参数名称  
     这里扩展一下：SpringBoot中的做法是在父pom文件中指定该参数，所以不需要显式指定该参数
7. 虽然目前为止mine-spring数据库持久化等功能还没有，准备做个示例项目来展示可用性
8. 一些Bug修复

---

- 0.0.2

1. 添加了`@ResponseBody`和`@RequestBody`注解解析
2. 重构了ViewResolver的解析过程，将功能拆分
3. 将View处理过程独立出去了，现在可以支持内置HTML和jsp视图渲染
4. 现在进入controller方法和退出controller方法之前可以使用resolver来对参数和返回值进行一定的处理
5. 一些bug修复

---

- 0.0.1

1. 最初的release版本
2. 基于注解的基础IoC容器
3. 基础webmvc功能
4. 基础aop功能
5. 文件上传下载处理
6. 基础的html、jsp模板解析功能，默认为html
7. 重构了ViewResolver和mvc的模板解析器初始化机制
8. 现在可以正确地支持使用jsp来构建页面，或者使用自带的html模板解析功能
9. 现在可以配置静态资源了，不会对静态资源进行解析，在web.xml中配置即可
10. 模板解析功能默认前缀为/WEB-INF/，默认后缀为.jsp
11. 更新了一下pom，现在可以同时发布源码和javadoc