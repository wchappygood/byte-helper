package org.example.bytehelper.agent.plugin.interceptor;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class InstanceMethodsTarget {
    private InstanceMethodsInterceptor instanceMethodsInterceptor;

    public InstanceMethodsTarget(InstanceMethodsInterceptor instanceMethodsInterceptor) {
        this.instanceMethodsInterceptor = instanceMethodsInterceptor;
    }

    @RuntimeType
    public Object target(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> call,
                         @Origin Method method) throws Exception {

        try {
            instanceMethodsInterceptor.beforeMethod(method, allArguments, method.getParameterTypes());
        } catch (Throwable e) {
            System.out.println(e);
        }
        Object call1 = call.call();

        try {
            instanceMethodsInterceptor.afterMethod(method, allArguments, method.getParameterTypes());
        } catch (Throwable e) {
            System.out.println(e);
        }

        return call1;
    }

}
