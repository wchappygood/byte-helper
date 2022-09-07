package org.example.logtrace.agent.plugin.interceptor;

import java.lang.reflect.Method;

public interface InstanceMethodsInterceptor {

    void beforeMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) throws Throwable;

    Object afterMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) throws Throwable;

}
