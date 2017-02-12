package parser;

import config.Configuration;
import config.ConfigurationFactory;
import config.IConfiguration;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by lamd on 2/11/2017.
 */
public class ConfigParser implements ConfigurationFactory {
    private String configPath;

    public ConfigParser() {
        this.configPath = "";
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * Reads a JSON file and returns its JSONObject.
     *
     * @return
     * @throws FileNotFoundException
     */
    private JSONObject readJsonObject() throws FileNotFoundException {
        if (this.configPath.isEmpty()) {
            return new JSONObject();
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(configPath));

            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }

            return new JSONObject(json.toString());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    /**
     * This method creates a new configuration based on the arguments passed
     * into the constructor
     *
     * @throws Exception
     */
    @Override
    public IConfiguration create() throws FileNotFoundException {
        IConfiguration config = Configuration.getInstance();
        JSONObject json = readJsonObject();
        config.populateMap("", json.toMap());
        return config;
    }
}
