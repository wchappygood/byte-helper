package org.example.bytehelper.agent.plugin.versionfortomcat;

import org.example.bytehelper.agent.plugin.AbstractClassEnhancePluginDefineDefault;

/**
 * @author SongJun
 * @description TODO:
 * @date 2022/9/26 09:47
 */
public class VersionInfoInstrumentation extends AbstractClassEnhancePluginDefineDefault {
    private String ENHANCE_CLASS = "org.apache.catalina.core.StandardWrapperValve";

    private String INTERCEPT_CLASS = "org.example.bytehelper.agent.plugin.versionfortomcat.VersionInfoInterceptor";

    private String ENHANCE_METHOD_NAME = "invoke";

    @Override
    public String getEnhanceClass() {
        return ENHANCE_CLASS;
    }

    @Override
    public String getInterceptClass() {
        return INTERCEPT_CLASS;
    }

    @Override
    public String getEnhanceMethodName() {
        return ENHANCE_METHOD_NAME;
    }
}
