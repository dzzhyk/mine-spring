# Release

---

- 0.0.2

1. 添加了@ResponseBody和@RequestBody注解解析
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