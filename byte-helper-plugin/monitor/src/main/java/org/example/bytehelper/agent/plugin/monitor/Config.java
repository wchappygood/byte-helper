package org.example.bytehelper.agent.plugin.monitor;

/**
 * @author SongJun
 * @description TODO:
 * @date 2022/9/30 10:09
 */
public class Config {
    public static class Monitor {
        public static class Version {
            public static String uri = "/versionInfo";
            public static String value = "unknown";
        }

        public static class Ds {
            public static String uri = "/dsInfo";
            public static String value = "unknown";
        }
    }
}
