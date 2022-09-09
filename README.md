# byte-helper
通过javaagent来修改字节码，实现想实现的功能。采用微内核的架构。

# 使用步骤
1. 编译
编译生成两类jar包  
核心jar包：byte-helper-core.jar  
插件包  
2. 工程引入jar包
```xml
<dependency>
    <groupId>org.example.byte.helper</groupId>
    <artifactId>byte-helper-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
```xml

<dependency>
    <groupId>org.example.byte.helper.plugin</groupId>
    <artifactId>tomcat-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
3. 启动
启动类加入
```java
public class Application {
    static {
        ByteHelperAgent.installAgent();
    }
    public static void main(String[] args) {
        
        SpringApplication.run(Application.class, args);

    }

}
```

# 新增插件
1. 在byte-helper-plugin新增module
2. 继承抽象类AbstractClassEnhancePluginDefine实现抽象方法
3. 实现接口InstanceMethodsInterceptor
4. 配置plugin.def