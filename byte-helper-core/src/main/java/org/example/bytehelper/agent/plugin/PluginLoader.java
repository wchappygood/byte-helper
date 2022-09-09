package org.example.bytehelper.agent.plugin;

import org.example.bytehelper.agent.plugin.loader.AgentClassLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader {

    public List<AbstractClassEnhancePluginDefine> loadPlugins() {
        AgentClassLoader.initDefaultLoader();

        PluginResourcesResolver pluginResourcesResolver = new PluginResourcesResolver();
        List<URL> resources = pluginResourcesResolver.getResources();

        if (resources == null || resources.size() == 0) {
            return new ArrayList();
        }

        List<PluginDefine> pluginDefines = new ArrayList<>();
        for (URL pluginUrl : resources) {
            pluginDefines.addAll(this.load(pluginUrl));
        }

        List<AbstractClassEnhancePluginDefine> plugins = new ArrayList<AbstractClassEnhancePluginDefine>();

        for (PluginDefine pluginDefine : pluginDefines) {
            try {
                AbstractClassEnhancePluginDefine abstractClassEnhancePluginDefine =
                        (AbstractClassEnhancePluginDefine) Class.forName(pluginDefine.getDefineClass()
                                , true, AgentClassLoader.getDefault()).newInstance();
                plugins.add(abstractClassEnhancePluginDefine);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("获取插件数："+plugins.size());

        return plugins;
    }

    private List<PluginDefine> load(URL url) {
        List<PluginDefine> pluginDefines = new ArrayList<>();

        try (InputStream inputStream = url.openStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String pluginDefineString;

            while ((pluginDefineString = bufferedReader.readLine()) != null) {
                PluginDefine pluginDefine = new PluginDefine(pluginDefineString);
                pluginDefines.add(pluginDefine);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return pluginDefines;
    }

}
