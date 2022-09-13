package org.example.bytehelper.agent.plugin.loader;

public class AgentClassLoader extends ClassLoader {

    private static AgentClassLoader DEFAULT_LOADER;

    public static AgentClassLoader getDefault() {
        return DEFAULT_LOADER;
    }

    public static void initDefaultLoader() {
        if (DEFAULT_LOADER == null) {
            synchronized (AgentClassLoader.class) {
                if (DEFAULT_LOADER == null) {
                    DEFAULT_LOADER = new AgentClassLoader(AgentClassLoader.class.getClassLoader());
                }
            }
        }
    }

    public AgentClassLoader(ClassLoader parent) {
        super(parent);
    }

}
