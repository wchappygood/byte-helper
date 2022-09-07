package org.example.logtrace.agent.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class AgentPackagePath {

    /**
     * 查找path
     *
     * @return File
     */
    public File findPath() {
        String classResourcePath = AgentPackagePath.class.getName().replaceAll("\\.", "/") + ".class";
        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();

            int insidePathIndex = urlString.indexOf("!");
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                File agentJarFile = null;
                try {
                    agentJarFile = new File(new URL(urlString).toURI());
                } catch (URISyntaxException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                if (agentJarFile.exists()) {
                    return agentJarFile.getParentFile();
                }
            } else {
                int prefixLength = "file:".length();
                String classLocation = urlString
                        .substring(prefixLength, urlString.length() - classResourcePath.length());
                return new File(classLocation);
            }
        }

        throw new RuntimeException("Can not locate agent jar file.");
    }
}
