package org.example.bytehelper.agent.plugin.interceptor.loader;

import org.example.bytehelper.agent.plugin.loader.AgentClassLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InterceptorInstanceLoader {
    private static ConcurrentHashMap<String, Object> INSTANCE_CACHE = new ConcurrentHashMap<String, Object>();

    private static Map<ClassLoader, ClassLoader> EXTEND_PLUGIN_CLASSLOADERS = new HashMap<ClassLoader, ClassLoader>();

    public <T> T load(String className,
                      ClassLoader targetClassLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (targetClassLoader == null) {
            targetClassLoader = InterceptorInstanceLoader.class.getClassLoader();
        }
        String instanceKey = className + "_OF_" + targetClassLoader.getClass()
                .getName() + "@" + Integer.toHexString(targetClassLoader
                .hashCode());
        Object inst = INSTANCE_CACHE.get(instanceKey);
        if (inst == null) {
            synchronized (InterceptorInstanceLoader.class) {
                ClassLoader pluginLoader;
                pluginLoader = EXTEND_PLUGIN_CLASSLOADERS.get(targetClassLoader);
                if (pluginLoader == null) {
                    pluginLoader = new AgentClassLoader(targetClassLoader);
                    EXTEND_PLUGIN_CLASSLOADERS.put(targetClassLoader, pluginLoader);
                }

                inst = Class.forName(className, true, pluginLoader).newInstance();
                if (inst != null) {
                    INSTANCE_CACHE.put(instanceKey, inst);
                }
            }
        }

        return (T) inst;
    }

}
