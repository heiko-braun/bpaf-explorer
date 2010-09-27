/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.bpm.monitor.model.hibernate;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author john.thompson
 *
 */
public class SchemaGenerator
{
    private AnnotationConfiguration cfg;
    private File output;

    public SchemaGenerator(String packageName, String output) throws Exception
    {
        cfg = new AnnotationConfiguration();
        cfg.setProperty("hibernate.hbm2ddl.auto","create");

        for(Class<?> clazz : getClasses(packageName))
        {
            cfg.addAnnotatedClass(clazz);
        }

        try {
            this.output = new File(output);
            if(!this.output.exists())
                this.output.mkdirs();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that actually creates the file.
     */
    private void generate(Dialect dialect)
    {
        String s = output.getAbsolutePath() + "/ddl_" + dialect.name().toLowerCase() + ".sql";
        cfg.setProperty("hibernate.dialect", dialect.getDialectClass());

        SchemaExport export = new SchemaExport(cfg);
        export.setDelimiter(";");
        export.setOutputFile(s);
        export.setFormat(true);
        export.setHaltOnError(true);
        export.execute(true, false, false, false);


        System.out.println("==================");
        System.out.println("DDL: "+s);
        System.out.println("==================");
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        String outputDir = args.length > 0  ? args[0] : System.getProperty("user.home");
        System.out.println("Output Dir: " + outputDir);

        SchemaGenerator gen = new SchemaGenerator("org.jboss.bpm.monitor.model.bpaf", outputDir);
        gen.generate(Dialect.MYSQL);
        gen.generate(Dialect.ORACLE);
        gen.generate(Dialect.HSQL);
    }

    /**
     * Utility method used to fetch Class list based on a package name.
     * @param packageName (should be the package containing your annotated beans.
     */
    private List<Class<?>> getClasses(String packageName) throws Exception
    {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        File directory = null;
        try
        {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = packageName.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(packageName + " (" + directory
                    + ") does not appear to be a valid package");
        }
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(packageName + '.'
                            + files[i].substring(0, files[i].length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(packageName
                    + " is not a valid package");
        }

        return classes;
    }

    /**
     * Holds the classnames of hibernate dialects for easy reference.
     */
    private static enum Dialect
    {
        ORACLE("org.hibernate.dialect.Oracle10gDialect"),
        MYSQL("org.hibernate.dialect.MySQLDialect"),
        HSQL("org.hibernate.dialect.HSQLDialect");

        private String dialectClass;
        private Dialect(String dialectClass)
        {
            this.dialectClass = dialectClass;
        }
        public String getDialectClass()
        {
            return dialectClass;
        }
    }
}