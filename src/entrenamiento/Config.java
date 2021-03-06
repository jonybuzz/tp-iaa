package entrenamiento;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	Properties prop = new Properties();
	private static Config instance = null;

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	public String getProperty(String str) {
		return prop.getProperty(str);
	}

	public Integer getIntProperty(String str) {
		try {
			return Integer.parseInt(prop.getProperty(str));
		} catch (NumberFormatException ex) {
			throw new RuntimeException("La property " + str + " no puede ser interpretada como numerica.", ex);
		}
	}

	public Config() {

		try(InputStream input = new FileInputStream("config.properties")) {
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
