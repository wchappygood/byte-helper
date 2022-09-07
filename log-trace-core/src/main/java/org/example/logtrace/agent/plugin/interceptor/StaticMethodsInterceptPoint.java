package org.example.logtrace.agent.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface StaticMethodsInterceptPoint {
    ElementMatcher<MethodDescription> getMethodsMatcher();

    String getStaticMethodsInterceptor();

    boolean isOverrideArgs();
}
