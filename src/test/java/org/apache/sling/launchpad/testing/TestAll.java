/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.launchpad.testing;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 *
 */
public class TestAll extends TestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestAll.class);

    @SuppressWarnings("unchecked")
    public static Test suite() {
        ClassLoader sysClassLoader = TestAll.class.getClassLoader();
        
        
        
        List<String> matchingClasses = new ArrayList<String>();
        // Get the URLs
        URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
        String testPattern = System.getProperty("integrationTestPattern",
                "**/launchpad/webapp/integrationtest/**/*Test");
        String testRegex = convertToRegex(testPattern);
        Pattern pattern = Pattern.compile(testRegex);
        LOGGER.info("Using Pattern " + testRegex);
        for (URL u : urls) {
            try {
                matchingClasses.addAll(scanFile(new File(u.toURI()), pattern));
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ClassLoader cl = TestAll.class.getClassLoader();
        Set<Class<TestCase>> classSet = new HashSet<Class<TestCase>>();
        for (String classFile : matchingClasses) {
            String className = classFileToName(classFile);
            try {
                Class<TestCase> c = (Class<TestCase>) cl.loadClass(className);
                if (!c.isInterface() && !Modifier.isAbstract(c.getModifiers()) ) {
                    LOGGER.info("Added "+className);
                    classSet.add(c);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        TestSuite suite = new TestSuite(classSet.toArray(new Class[classSet.size()]),"Sling Integration Tests matching "+testPattern);
      
        return suite;
    }

    /**
     * @param classFile
     * @return
     */
    private static String classFileToName(String classFile) {
        String className =  classFile.substring(0, classFile.length() - (".class".length()))
                .replace('/', '.');
        if ( className.charAt(0) == '.' ) {
            className = className.substring(1);
        }
        return className;
    }

    /**
     * @param testPattern
     * @return
     */
    private static String convertToRegex(String testPattern) {
        return testPattern.replace("**/", ".a?").replace("*", ".a?").replace(
                ".a?", ".*?").replace("/", "\\/")
                + "\\.class$";
    }

    /**
     * @param u
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private static List<String> scanFile(File f, Pattern pattern)
            throws URISyntaxException, IOException {
        List<String> classPathMatches = new ArrayList<String>();
        if (f.isFile()) {
            scanJar(f, pattern, classPathMatches);
        } else {
            scanDir(f, pattern, classPathMatches, f.getPath().length());
        }
        return classPathMatches;
    }

    /**
     * @param f
     * @param pattern
     * @return
     */
    private static void scanDir(File f, Pattern pattern,
            List<String> classPathMatches, int trim) {
        if (f.isFile()) {
            String name = f.getPath().substring(trim);
            if (pattern.matcher(name).matches()) {
                classPathMatches.add(name);
            }
        } else {
            for (File cf : f.listFiles()) {
                scanDir(cf, pattern, classPathMatches, trim);
            }
        }
    }

    /**
     * @param u
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    private static void scanJar(File f, Pattern pattern,
            List<String> classPathMatches) throws URISyntaxException,
            IOException {
        JarFile jarFile = new JarFile(f);
        for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
            JarEntry je = e.nextElement();
            String entryName = je.getName();
            if (pattern.matcher(entryName).matches()) {
                classPathMatches.add(entryName);
            }
        }
        jarFile.close();
    }

}
