package org.example.logtrace.agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.example.logtrace.agent.plugin.AbstractClassEnhancePluginDefine;
import org.example.logtrace.agent.plugin.AgentPackagePath;
import org.example.logtrace.agent.plugin.PluginLoader;
import org.example.logtrace.agent.plugin.match.NameMatch;

public class LogTraceAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        PluginLoader pluginLoader = new PluginLoader();
        List<AbstractClassEnhancePluginDefine> abstractClassEnhancePluginDefines = pluginLoader.loadPlugins();

        ElementMatcher.Junction junction = ElementMatchers.not(ElementMatchers.isInterface());

        for (AbstractClassEnhancePluginDefine abstractClassEnhancePluginDefine : abstractClassEnhancePluginDefines) {
            NameMatch nameMatch = abstractClassEnhancePluginDefine.filterClass();
            junction.or(ElementMatchers.named(nameMatch.getClassName()));
        }

        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader,
                                                    JavaModule javaModule) {
                DynamicType.Builder<?> newBuilder = builder;
                String typeName = typeDescription.getTypeName();
                for (AbstractClassEnhancePluginDefine abstractClassEnhancePluginDefine : abstractClassEnhancePluginDefines) {
                    if (typeName.equals(abstractClassEnhancePluginDefine.filterClass().getClassName())) {
                        try {
                            newBuilder = abstractClassEnhancePluginDefine.interceptInstanceMethods(typeDescription, newBuilder, classLoader);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            newBuilder = abstractClassEnhancePluginDefine.interceptStaticMethods(typeDescription, newBuilder, classLoader);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return newBuilder;
            }
        };

        final ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(true));

        AgentBuilder agentBuilder = new AgentBuilder.Default(byteBuddy);
        agentBuilder.type(junction)
                .transform(transformer)
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(new Listener())
                .installOn(instrumentation);

    }

    private static class Listener implements AgentBuilder.Listener {
        @Override
        public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

        }

        @Override
        public void onTransformation(final TypeDescription typeDescription,
                                     final ClassLoader classLoader,
                                     final JavaModule module,
                                     final boolean loaded,
                                     final DynamicType dynamicType) {

            File debuggingClassesRootPath = null;
            try {
                if (debuggingClassesRootPath == null) {
                    debuggingClassesRootPath = new File(new AgentPackagePath().findPath(), "/debugging");
                    if (!debuggingClassesRootPath.exists()) {
                        debuggingClassesRootPath.mkdir();
                    }
                }
                dynamicType.saveIn(debuggingClassesRootPath);
            } catch (Throwable t) {
                System.out.println(t.getMessage());
            }
        }

        @Override
        public void onIgnored(final TypeDescription typeDescription,
                              final ClassLoader classLoader,
                              final JavaModule module,
                              final boolean loaded) {

        }

        @Override
        public void onError(final String typeName,
                            final ClassLoader classLoader,
                            final JavaModule module,
                            final boolean loaded,
                            final Throwable throwable) {
        }

        @Override
        public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        }
    }
}
