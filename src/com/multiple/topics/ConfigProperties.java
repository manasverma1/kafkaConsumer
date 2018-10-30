package com.multiple.topics;

import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {

	private static ConfigProperties props= null;

	private Properties properties;

	private ConfigProperties() {
		properties = new Properties();
		try {
			properties.load(ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ConfigProperties getInstance() 
	{ 
		if (props == null) 
			props = new ConfigProperties();
		return props; 
	}

	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}
	/*public Properties getProperties() {
		return properties;
	}*/
}
