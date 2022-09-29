package org.example.bytehelper.agent.plugin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.bytehelper.agent.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.example.bytehelper.agent.plugin.interceptor.InstanceMethodsTarget;
import org.example.bytehelper.agent.plugin.interceptor.StaticMethodsInterceptPoint;
import org.example.bytehelper.agent.plugin.interceptor.StaticMethodsTarget;
import org.example.bytehelper.agent.plugin.interceptor.loader.InterceptorInstanceLoader;
import org.example.bytehelper.agent.plugin.match.NameMatch;

/**
 * @author SongJun
 * @description TODO:
 * @date 2022/9/26 10:04
 */
public abstract class AbstractClassEnhancePluginDefineDefault extends AbstractClassEnhancePluginDefine {
    private InterceptorInstanceLoader interceptorInstanceLoader = new InterceptorInstanceLoader();

    abstract public String getEnhanceClass();
    abstract public String getInterceptClass();
    abstract public String getEnhanceMethodName();

    public NameMatch filterClass() {
        return new NameMatch(getEnhanceClass());
    }

    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(getEnhanceMethodName());
                    }

                    @Override
                    public String getInstanceMethodsInterceptor() {
                        return getInterceptClass();
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }

    public StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return null;
    }
}
