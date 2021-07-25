package com.epam.esm.hibernate_util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HibernateUtil {
    public static final String PROP_FILE = "hibernateproperties";
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactoryWithAnnotatedClasses(Class... classes) {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = loadSettings();
                configuration.setProperties(settings);
                for (Class c : classes) {
                    configuration.addAnnotatedClass(c);
                }
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                //todo logging
            }
        }
        return sessionFactory;
    }

    private static Properties loadSettings() {
        Properties settings = new Properties();
        try {
            settings.load(new FileReader(PROP_FILE));
        } catch (IOException e) {
            //todo logging
        }
        return settings;
    }
}
