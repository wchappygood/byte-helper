package org.example.logtrace.agent.plugin;

public class PluginDefine {

    private String name;

    private String defineClass;

    public PluginDefine(String define) {

        if (define == null || define.length() == 0) {
            throw new RuntimeException("define");
        }

        String[] pluginDefine = define.split("=");
        if (pluginDefine.length != 2) {
            throw new RuntimeException(define);
        }

        this.name = pluginDefine[0];
        this.defineClass = pluginDefine[1];
    }

    public String getName() {
        return name;
    }

    public String getDefineClass() {
        return defineClass;
    }
}
