package org.example.bytehelper.agent.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface InstanceMethodsInterceptPoint {
    ElementMatcher<MethodDescription> getMethodsMatcher();

    String getInstanceMethodsInterceptor();

    boolean isOverrideArgs();

}
