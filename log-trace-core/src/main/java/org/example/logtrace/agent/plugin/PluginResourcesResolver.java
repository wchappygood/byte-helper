package org.example.logtrace.agent.plugin;

import org.example.logtrace.agent.plugin.loader.AgentClassLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PluginResourcesResolver {
    private final static String EDF = "plugin.def";

    public List<URL> getResources() {
        List<URL> cfgUrlPaths = new ArrayList<>();
        try {
            Enumeration<URL> urls = AgentClassLoader.getDefault().getResources(EDF);
            while (urls.hasMoreElements()) {
                URL pluginUrl = urls.nextElement();
                cfgUrlPaths.add(pluginUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cfgUrlPaths;
    }
}
