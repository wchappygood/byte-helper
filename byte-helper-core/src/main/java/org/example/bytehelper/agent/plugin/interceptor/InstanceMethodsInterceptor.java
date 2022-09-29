package org.example.bytehelper.agent.plugin.interceptor;

import java.lang.reflect.Method;

public interface InstanceMethodsInterceptor {

    Object beforeMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) throws Throwable;

    Object afterMethod(Method method, Object[] allArguments, Class<?>[] argumentsTypes) throws Throwable;

}
