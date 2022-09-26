package org.example.bytehelper.agent.plugin.versionfortomcat;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
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

    @Override
    public Object beforeMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) {
        Object ret = null;
        Request request = (Request) allArguments[0];
        if(VERSION_URI.equals(request.getRequestURI())){
            Response response = (Response) allArguments[1];
            String agentArgs = System.getProperty("agentArgs");
            if(agentArgs != null){
                System.out.println("查询版本, 版本标记:"+agentArgs);
                try {
                    PrintWriter writer = response.getWriter();
                    writer.print(agentArgs);
                    writer.close();
                    response.flushBuffer();
                    ret = agentArgs;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("查询版本, 未获取到版本标记");
            }

        }
        return ret;
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) {
        return null;
    }
}
