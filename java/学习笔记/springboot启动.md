

## springmvc配置了web.xml和app-context.xml的启动过程
1. 在web.xml中配置ContextLoaderListener和DispatcherServlet
2. 在servlet-context.xml中配置handlerMapping和handlerAdapter，或者mvc:annotation-driven

## DispatcherServlet处理请求的流程
1. 根据请求的uri、类型、context-type、accept等属性找到可以处理这个请求的requestHandler(对rest请求来说，就是xxRestController.xxMethod)。核心是handlertMapping。
2. 读取request，转换为requestHanlder的入参。核心是handlerAdapter。
3. 调用requstHanlder进行业务处理。
4. 对于rest请求，将requestHandler的返回值写入到response中。
5. 如果非rest请求，requestHandler返回modelAndView，再找对应的视图进行渲染。

## springmvc无web.xml启动过程
1. SpringServletContainerInitializer实现了Servlet 3.0的服务接口，/META-INF/services/javax.servlet.ServletContainerInitializer  
容器启动的时候，会把类路径下的HandlerTypes和servletContext传给这个接口
```java
@HandlesTypes(WebApplicationInitializer.class) // 
public class SpringServletContainerInitializer implements ServletContainerInitializer {
    @Override
	public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
			throws ServletException {
        ...
    }
```
2. 开发者写一个类继承AbstractAnnotationConfigDispatcherServletInitializer，然后实现对应的接口，返回rootContext\servletContext\servletMapping\servletFilter的config类
3. 在servetMapping的config类中打上@EnableWebMvc等注解
```java
@Configuration
@ComponentScan(basePackages = "org.springframework.samples.mvc")
@EnableWebMvc
@EnableScheduling
public class WebMvcConfig implements WebMvcConfigurer {
    ...
}
```
**TODO**:这几个注解的机制是什么？为什么打上@EnableWebMvc注解就可以实现创建一堆bean？WebMvcConfigurer接口又是干嘛的？

## springboot的componentscan会扫描哪些包？
默认扫描打了@SpringBootApplication注解的类所在的package及其子package

## springboot如何创建一个容器，注册DispatchServlet，加载springmvc的requestMapping和handlerAdapter？
1. springboot启动过程会单独创建一个线程`RestartLauncher`来创建context，原线程最后会抛出一个异常默默地结束生命。这个过程中，它给新线程设置了ClassLoader。
2. 创建context，这个过程中会创建一个内嵌的容器
```java
org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.createWebServer()
```
3. 内嵌容器不实现Servlet 3.0的ServletContainerInitializer机制。容器创建完之后，会调用`this::selfInitialize`。在这个方法中完成DispatchServlet的创建和注册。
```java
org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.selfInitialize(ServletContext)
```
4. 在收到第一个请求的时候，初始化DispatchServlet，initHandlerMapping。


[spring-web](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#spring-web)
[spring-boot内嵌容器](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-embedded-container)
[一位老兄写的spring-boot分析](https://fangjian0423.github.io/)