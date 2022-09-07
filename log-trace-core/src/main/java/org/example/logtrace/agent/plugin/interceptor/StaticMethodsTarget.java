package org.example.logtrace.agent.plugin.interceptor;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class StaticMethodsTarget {

    private StaticMethodsInterceptor staticMethodsInterceptor;

    public StaticMethodsTarget(StaticMethodsInterceptor staticMethodsInterceptor) {
        this.staticMethodsInterceptor = staticMethodsInterceptor;
    }

    @RuntimeType
    public Object target(@This Object obj, @AllArguments Object[] allArguments, @SuperCall Callable<?> call,
                         @Origin Method method) throws Exception {

        return call.call();
    }
}
