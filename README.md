# byte-helper
通过javaagent的静态挂载来修改字节码，实现想实现的功能。采用微内核的架构。

# 使用步骤
1. 编译
编译完成，生成两类jar包
核心jar包：byte-helper-core-1.0-SNAPSHOT-jar-with-dependencies.jar
插件包
2. 创建目录
mkdir ${user_path}/agent/plugins ，其中user_path自定义
3. 核心包放在${user_path}/agent/，插件包放在${user_path}/agent/plugins/
4. java -javaagent:${user_path}/agent/byte-helper-core-1.0-SNAPSHOT-jar-with-dependencies.jar -jar app.jar

# 新增插件
1. 在byte-helper-plugin新增module
2. 继承抽象类AbstractClassEnhancePluginDefine实现抽象方法
3. 实现接口InstanceMethodsInterceptor
4. 配置plugin.def

# 插件读取环境变量配置
byte_helper_${插件名称}_${变量名}=${变量值}