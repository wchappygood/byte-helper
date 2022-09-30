package org.example.bytehelper.agent.plugin.monitor;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.example.bytehelper.agent.config.ConfigInitializer;
import org.example.bytehelper.agent.plugin.interceptor.InstanceMethodsInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author SongJun
 * @description TODO:
 * @date 2022/9/26 09:48
 */
public class VersionInfoInterceptor implements InstanceMethodsInterceptor {
    private static final String VERSION_URI = "/versionInfo";
    private static final String VERSION_MARK_KEY = "branchOrTag";
    static {
        try {
            ConfigInitializer.initializeConfig(Config.class, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object beforeMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) {
        Object ret = null;
        Request request = (Request) allArguments[0];
        if(Config.Monitor.version_uri.equals(request.getRequestURI())){
            String branchOrTag = Config.Monitor.version_value;
            System.out.println("查询版本:"+branchOrTag);
            returnString((Response) allArguments[1], branchOrTag);
            ret = branchOrTag;
        } else if(Config.Monitor.datasource_uri.equals(request.getRequestURI())){
            String dataSource = Config.Monitor.datasource_value;
            System.out.println("查询数据源:"+dataSource);
            returnString((Response) allArguments[1], dataSource);
            ret = dataSource;
        }
        return ret;
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) {
        return null;
    }

    //返回字符串
    private void returnString(Response response, String msg){
        try {
            PrintWriter writer = response.getWriter();
            writer.print(msg);
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
