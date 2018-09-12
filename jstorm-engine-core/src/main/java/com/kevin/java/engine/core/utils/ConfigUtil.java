package com.kevin.java.engine.core.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {

  private static final String DEFAULT_FILE_NAME = "config.properties";
  private static Properties properties = new Properties();

  static {
    properties = getProperties(DEFAULT_FILE_NAME);
  }

  public static Properties getDefaultConfig() {
    return properties;
  }

  public static Map<String,Object> getMapFromConfig(String configFile) {
    Map<String,Object> config = new HashMap<>();
    Properties prop = getProperties(configFile);
    Iterator i$ = prop.entrySet().iterator();

    while(i$.hasNext()) {
      Map.Entry<Object, Object> entry = (Map.Entry)i$.next();
      config.put((String)entry.getKey(), entry.getValue());
    }
    return config;
  }

  public static Properties getProperties(String fileName) {
    InputStream inputStream = null;
    Properties properties = new Properties();
    try {
      ClassLoader classLoader = ConfigUtil.class.getClassLoader();
      inputStream = classLoader.getResourceAsStream(fileName);
      if (null != inputStream) {
        properties.load(inputStream);
      }
      else {
        System.out.println("Unable to find configuration file: config.properties");
        throw new FileNotFoundException("config.properties not found in classpath");
      }
    }
    catch (IOException e) {
      System.out.println("Exception:" + e.getMessage());
    }
    finally {
      if (null != inputStream) {
        try {
          inputStream.close();
        }
        catch (IOException e) {
          //Ignore.
        }
      }
    }
    return properties;
  }

}