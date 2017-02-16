package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import config.Configuration;
import config.IConfiguration;

public class PropertiesConfiguration implements IConfigurationParser{

	private String path;
	public PropertiesConfiguration() {
		path="";
	}
		
	@Override
	public IConfiguration create() throws FileNotFoundException {
		IConfiguration config = Configuration.getInstance();
        Map<String,Object> map = parsePropertiesFile(path);
        config.populateMap("", map);
        return config;
    }
	
	private Map<String,Object> parsePropertiesFile(String propertiesPath) throws FileNotFoundException {
		Map<String,Object> ret = new HashMap<String,Object>();
		Scanner scanner = null;
		if (propertiesPath.isEmpty())
			return ret;
		try {
		    scanner = new Scanner(new File(propertiesPath));
		
		    while (scanner.hasNextLine()) {
		    	String[] line = scanner.nextLine().split("=");
		    	if(line.length == 2){
		    		String[] multi = line[1].trim().split(",");
		    		if (multi.length <= 1){
		    			ret.put(line[0].trim(), line[1].trim());
		    		} else {
		    			ret.put(line[0].trim(), String.join(" ", multi));
		    		}
		    	}
		    }
		
		} finally {
		    if (scanner != null) {
		        scanner.close();
		    }
		}
		return ret;
	}

	@Override
	public void setConfigPath(String path) {
		this.path = path;
		
	}

}
