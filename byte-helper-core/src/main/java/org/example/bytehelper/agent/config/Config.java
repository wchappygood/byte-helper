package org.example.bytehelper.agent.config;


import org.example.bytehelper.agent.plugin.AgentPackagePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {
    private static final String DEFAULT_CONFIG_FILE_NAME = "/config/agent.config";

    private Properties agentSettings = new Properties();

    private InputStreamReader loadConfig() {
        File configFile = new File(new AgentPackagePath().findPath(), DEFAULT_CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile()) {
            try {
                return new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Failed to load agent.config", e);
            }
        }
        throw new RuntimeException("Failed to load agent.config");
    }

    public void initializeCoreConfig() {
        try (InputStreamReader configInputStreamReader = this.loadConfig()) {
            agentSettings.load(configInputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
