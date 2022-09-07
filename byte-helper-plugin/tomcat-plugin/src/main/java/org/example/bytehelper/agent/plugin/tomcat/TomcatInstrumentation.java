package org.example.bytehelper.agent.plugin.tomcat;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.bytehelper.agent.plugin.AbstractClassEnhancePluginDefine;
import org.example.bytehelper.agent.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.example.bytehelper.agent.plugin.interceptor.StaticMethodsInterceptPoint;
import org.example.bytehelper.agent.plugin.match.NameMatch;

public class TomcatInstrumentation extends AbstractClassEnhancePluginDefine {
    private static final String ENHANCE_CLASS = "org.apache.catalina.core.StandardHostValve";

    private static final String INTERCEPT_CLASS = "org.example.bytehelper.agent.plugin.tomcat.TomcatInterceptor";

    @Override
    public NameMatch filterClass() {
        return new NameMatch(ENHANCE_CLASS);
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named("invoke");
                    }

                    @Override
                    public String getInstanceMethodsInterceptor() {
                        return INTERCEPT_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    @Override
    public StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return null;
    }
}
