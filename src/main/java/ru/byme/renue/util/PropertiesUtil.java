package ru.byme.renue.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertiesUtil() {
    }

    public static int getProperties(String key) {
        if (key.equals("columnNumber")) {
            return Integer.parseInt(PROPERTIES.getProperty(key));
        } else
            return Integer.parseInt(key);
    }
}
