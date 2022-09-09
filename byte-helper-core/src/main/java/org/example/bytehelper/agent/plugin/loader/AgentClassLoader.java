package org.example.bytehelper.agent.plugin.loader;

import org.example.bytehelper.agent.plugin.AgentPackagePath;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AgentClassLoader extends ClassLoader {
    public final static String EDF = "plugin.def";

    private static AgentClassLoader DEFAULT_LOADER;

    private File classpath;

    private LinkedList<Jar> allJars;

    public static AgentClassLoader getDefault() {
        return DEFAULT_LOADER;
    }

    public static void initDefaultLoader() {
        if (DEFAULT_LOADER == null) {
            synchronized (AgentClassLoader.class) {
                if (DEFAULT_LOADER == null) {
                    DEFAULT_LOADER = new AgentClassLoader(AgentClassLoader.class.getClassLoader());
                }
            }
        }
    }

    public AgentClassLoader(ClassLoader parent) {
        super(parent);
        File agentDictionary = new AgentPackagePath().findPath();
        classpath = agentDictionary;
        System.out.println(classpath);
    }

    @Override
    protected Class findClass(String name) {
        String path = name.replace(".", "/").concat(".class");
        LinkedList<Jar> allJars = this.getAllJars();

        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(path);
            if (jarEntry == null) {
                continue;
            }
            try {
                URL classFileUrl = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] data;
                try (BufferedInputStream is = new BufferedInputStream(classFileUrl.openStream());
                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                    int ch;
                    while ((ch = is.read()) != -1) {
                        byteArrayOutputStream.write(ch);
                    }
                    data = byteArrayOutputStream.toByteArray();
                    return defineClass(name, data, 0, data.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Can not find " + name);
    }

    @Override
    protected URL findResource(String name) {
        LinkedList<Jar> allJars = this.getAllJars();
        for (Jar jar : allJars) {
            JarEntry jarEntry = jar.jarFile.getJarEntry(name);
            if (jarEntry != null) {
                try {
                    return new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) {
        LinkedList<URL> allResources = new LinkedList<>();
        LinkedList<Jar> allJars = this.getAllJars();
        for (Jar jar : allJars) {
            JarEntry entry = jar.jarFile.getJarEntry(name);
            if (entry != null) {
                try {
                    allResources.add(new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + name));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Iterator<URL> iterator = allResources.iterator();

        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };

    }

    private LinkedList<Jar> getAllJars() {

        if (allJars == null) {
            synchronized (AgentClassLoader.class) {
                if (allJars == null) {
                    allJars = new LinkedList<>();
                    if (classpath.exists() && classpath.isDirectory()) {
                        String[] jarFileNames = classpath.list((dir, name) -> name.endsWith(".jar"));
                        for (String filename : jarFileNames) {
                            try {
                                File file = new File(classpath, filename);
                                JarFile jarFile = new JarFile(file);
                                if (Objects.nonNull(jarFile.getJarEntry(EDF))){
                                    Jar jar = new Jar(jarFile, file);
                                    allJars.add(jar);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
        return allJars;
    }

    private static class Jar {
        private final JarFile jarFile;
        private final File sourceFile;

        public Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }
    }
}
