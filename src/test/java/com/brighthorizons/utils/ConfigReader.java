package com.brighthorizons.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Utility to read properties from config file
public class ConfigReader {
    private static final Properties properties = new Properties();
    private static boolean isInitialized = false;

    // Private constructor prevents creating instances
    private ConfigReader() {
    }

    // Load properties from config file
    public static synchronized void initialize() {
        if (!isInitialized) {
            try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    try (FileInputStream fileInput = new FileInputStream("src/test/resources/config.properties")) {
                        properties.load(fileInput);
                    }
                } else {
                    properties.load(input);
                }
                isInitialized = true;
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config.properties", e);
            }
        }
    }

    // Get property value
    public static String getProperty(String key) {
        if (!isInitialized) {
            initialize();
        }
        return properties.getProperty(key);
    }

    // Get property with default value
    public static String getProperty(String key, String defaultValue) {
        if (!isInitialized) {
            initialize();
        }
        return properties.getProperty(key, defaultValue);
    }

    // Get property as integer
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Get property as boolean
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    // Get comma-separated property as array
    public static String[] getArrayProperty(String key) {
        String value = getProperty(key);
        return value != null ? value.split(",") : new String[0];
    }
}