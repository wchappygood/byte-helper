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

public abstract class AbstractClassEnhancePluginDefine {

    private InterceptorInstanceLoader interceptorInstanceLoader = new InterceptorInstanceLoader();

    public abstract NameMatch filterClass();

    public DynamicType.Builder<?> interceptInstanceMethods(TypeDescription typeDescription,
                                                           DynamicType.Builder<?> builder,
                                                           ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        DynamicType.Builder<?> newBuilder = builder;

        String typeName = typeDescription.getTypeName();
        if (!typeName.equals(this.filterClass().getClassName())) {
            return builder;
        }

        InstanceMethodsInterceptPoint[] instanceMethodsInterceptPoints = this.getInstanceMethodsInterceptPoints();
        if (instanceMethodsInterceptPoints != null && instanceMethodsInterceptPoints.length > 0) {
            for (InstanceMethodsInterceptPoint instanceMethodsInterceptPoint : instanceMethodsInterceptPoints) {
                ElementMatcher.Junction<MethodDescription> junction = ElementMatchers.not(ElementMatchers.isStatic())
                        .and(instanceMethodsInterceptPoint.getMethodsMatcher());
                newBuilder = newBuilder.method(junction)
                        .intercept(MethodDelegation.withDefaultConfiguration()
                                .to(new InstanceMethodsTarget(interceptorInstanceLoader.load(instanceMethodsInterceptPoint.getInstanceMethodsInterceptor(),classLoader))));
            }
        }
        return newBuilder;
    }

    public DynamicType.Builder<?> interceptStaticMethods(TypeDescription typeDescription,
                                                         DynamicType.Builder<?> builder,
                                                         ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        DynamicType.Builder<?> newBuilder = builder;

        String typeName = typeDescription.getTypeName();
        if (!typeName.equals(this.filterClass().getClassName())) {
            return builder;
        }

        StaticMethodsInterceptPoint[] staticMethodsInterceptPoints = this.getStaticMethodsInterceptPoints();
        if (staticMethodsInterceptPoints != null && staticMethodsInterceptPoints.length > 0) {
            for (StaticMethodsInterceptPoint staticMethodsInterceptPoint : staticMethodsInterceptPoints) {
                newBuilder = newBuilder.method(ElementMatchers.isStatic().and(staticMethodsInterceptPoint.getMethodsMatcher()))
                        .intercept(MethodDelegation.withDefaultConfiguration()
                                .to(new StaticMethodsTarget(interceptorInstanceLoader.load(staticMethodsInterceptPoint.getStaticMethodsInterceptor(),classLoader))));
            }
        }
        return newBuilder;
    }

    public abstract InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints();

    public abstract StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints();
}
