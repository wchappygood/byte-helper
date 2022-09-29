package org.example.bytehelper.agent.config;

import java.util.Map;
import java.util.Properties;

public class ConfigInitializer {
    private static String ENV_KEY_PREFIX = "byte_helper";

    private static Properties AGENT_SETTINGS;

    public static void initializeConfig(String agentArgs) {
        AGENT_SETTINGS = new Properties();

        overrideConfigByEnvProp();

    }

    /**
     * 读取环境变量的值
     */
    private static void overrideConfigByEnvProp() {
        Map<String, String> envProperties = System.getenv();
        for (Map.Entry<String, String> prop : envProperties.entrySet()) {
            String key = prop.getKey();
            if (ENV_KEY_PREFIX.startsWith(key)) {
                String realKey = key.substring(ENV_KEY_PREFIX.length());
                AGENT_SETTINGS.put(realKey, prop.getValue());
            }
        }
    }
}
