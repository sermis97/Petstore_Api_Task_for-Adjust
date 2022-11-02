package io.petstore.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.Properties;

public class ConfigurationReader {
    private static Properties properties;

    private static void initProperties(String filePath) {
        try {
            try {
                FileInputStream input = new FileInputStream(filePath);
                properties = new Properties();
                properties.load(input);
                input.close();
            } catch (FileNotFoundException notFoundException) {
                notFoundException.printStackTrace();
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createPropertiesFile(String pathAndName) {
        try {
            File file = new File(pathAndName);
            if (file.exists()) {
                throw new FileAlreadyExistsException("File already exists");
            } else {
                FileOutputStream output = new FileOutputStream(pathAndName);
                output.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String endpoint(String keyName) {
        initProperties("src/test/resources/configuration.properties");
        return properties.getProperty(keyName);
    }

    public static String get(String keyName) {
        initProperties("src/test/resources/configuration.properties");
        return properties.getProperty(keyName);
    }
}
