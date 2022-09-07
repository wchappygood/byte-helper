package org.example.logtrace.agent.plugin;

import org.apache.catalina.connector.Request;
import org.apache.tomcat.util.http.Parameters;
import org.example.logtrace.agent.plugin.interceptor.InstanceMethodsInterceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;

public class TomcatInterceptor implements InstanceMethodsInterceptor {

    @Override
    public void beforeMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) {
        Request request = (Request) allArguments[0];
        String traceId = request.getHeader("traceId");
        System.out.println(traceId);


        final org.apache.coyote.Request coyoteRequest = request.getCoyoteRequest();
        final Parameters parameters = coyoteRequest.getParameters();
        for (final Enumeration<String> names = parameters.getParameterNames(); names.hasMoreElements(); ) {
            final String name = names.nextElement();
            System.out.println(name);
        }
    }

    @Override
    public Object afterMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) {
        return null;
    }
}
