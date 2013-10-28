package com.acquire.board;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Labels {
	private static List <String> labels;
	private static Labels l;
	private Properties properties;
	
	private Labels() {
try {
properties = new Properties();

InputStream inputStream =
this.getClass().getClassLoader().getResourceAsStream("properties.properties");

if (inputStream == null)
{
throw new FileNotFoundException("property file properties.properties"
+ "' not found in the classpath");
}

properties.load(inputStream);
// properties.load(new FileInputStream("properties.properties"));
labels = new ArrayList<>(Arrays.asList(properties.getProperty("labels").split(",")));
} catch (FileNotFoundException e) {
e.printStackTrace();
} catch (IOException e) {
e.printStackTrace();
}
}
		
	public static synchronized List<String> getLabels() {
		if (l == null) {
			l = new Labels();
		}
		return Labels.labels;
	}
}
