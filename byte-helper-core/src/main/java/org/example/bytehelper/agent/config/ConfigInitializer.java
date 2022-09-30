package org.example.bytehelper.agent.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ConfigInitializer {
    private static String ENV_KEY_PREFIX = "byte_helper_";

    private static Properties AGENT_SETTINGS;

    public static void loadConfig(String agentArgs) throws IllegalAccessException {
        AGENT_SETTINGS = new Properties();

        overrideConfigByEnvProp();

        initializeConfig(Config.class, null);

    }

    /**
     * 给Config的field赋值
     *
     * @param configClass
     */
    public static void initializeConfig(Class configClass, String parent) throws IllegalAccessException {
        if (AGENT_SETTINGS == null) {
            throw new RuntimeException("AGENT_SETTINGS未初始化");
        }
        String localParent = parent;
        if (Objects.nonNull(localParent)) {
            localParent = localParent + "_";
        } else {
            localParent = "";
        }

        for (Field field : configClass.getFields()) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                String configKey = (localParent + field.getName()).toLowerCase();
                String value = AGENT_SETTINGS.getProperty(configKey);
                if (Objects.nonNull(value)) {
                    field.set(null, convertToTypicalType(field.getType(), value));
                }
            }
        }

        for (Class innerConfig : configClass.getClasses()) {
            initializeConfig(innerConfig, localParent + innerConfig.getSimpleName());
        }
    }

    /**
     * 读取环境变量的值
     */
    private static void overrideConfigByEnvProp() {
        Map<String, String> envProperties = System.getenv();
        for (Map.Entry<String, String> prop : envProperties.entrySet()) {
            String key = prop.getKey();
            if (key.startsWith(ENV_KEY_PREFIX)) {
                String realKey = key.substring(ENV_KEY_PREFIX.length());
                AGENT_SETTINGS.put(realKey, prop.getValue());
            }
        }
    }

    private static Object convertToTypicalType(Type type, String value) {
        if (value == null || type == null) {
            return null;
        }

        Object result = null;
        if (String.class.equals(type)) {
            result = value;
        } else if (int.class.equals(type) || Integer.class.equals(type)) {
            result = Integer.valueOf(value);
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            result = Long.valueOf(value);
        } else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
            result = Boolean.valueOf(value);
        } else if (float.class.equals(type) || Float.class.equals(type)) {
            result = Float.valueOf(value);
        } else if (double.class.equals(type) || Double.class.equals(type)) {
            result = Double.valueOf(value);
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isEnum()) {
                result = Enum.valueOf((Class<Enum>) type, value.toUpperCase());
            }
        }
        return result;
    }
}
