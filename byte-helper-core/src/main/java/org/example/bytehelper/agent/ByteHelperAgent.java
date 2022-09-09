package org.example.bytehelper.agent;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;
import org.example.bytehelper.agent.plugin.AbstractClassEnhancePluginDefine;
import org.example.bytehelper.agent.plugin.PluginLoader;
import org.example.bytehelper.agent.plugin.match.NameMatch;

import java.io.File;
import java.util.List;

public class ByteHelperAgent {

    public static void installAgent() {

        ByteBuddyAgent.install();

        PluginLoader pluginLoader = new PluginLoader();
        List<AbstractClassEnhancePluginDefine> abstractClassEnhancePluginDefines = pluginLoader.loadPlugins();

        ElementMatcher elementMatcher = new ElementMatcher<NamedElement>() {
            @Override
            public boolean matches(NamedElement namedElement) {
                for (AbstractClassEnhancePluginDefine abstractClassEnhancePluginDefine : abstractClassEnhancePluginDefines) {
                    NameMatch nameMatch = abstractClassEnhancePluginDefine.filterClass();
                    if (namedElement.getActualName().equals(nameMatch.getClassName())) {
                        return true;
                    }
                }
                return false;
            }
        };


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
        agentBuilder.type(elementMatcher)
                .transform(transformer)
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(new Listener())
                .installOnByteBuddyAgent();

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
                    debuggingClassesRootPath = new File("/Users/admin/Desktop/debugging");
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
