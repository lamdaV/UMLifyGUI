package parser;

import config.Configuration;
import config.IConfiguration;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * JSON Configuration Parser.
 * <p>
 * Created by lamd on 2/11/2017.
 */
public class JSONParser implements IParser {
    private String configPath;

    /**
     * Constructs a JSON Configuration Parser.
     */
    public JSONParser() {
        this.configPath = "";
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * Reads a JSON file and returns its JSONObject.
     *
     * @return JSONObject representing the JSON.
     * @throws FileNotFoundException If the Scanner cannot find the JSON file path.
     */
    private JSONObject readJsonObject() throws FileNotFoundException {
        if (this.configPath.isEmpty()) {
            return new JSONObject();
        }

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(this.configPath));

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
     * @throws FileNotFoundException Configuration Path is invalid.
     */
    @Override
    public IConfiguration create() throws FileNotFoundException {
        IConfiguration config = Configuration.getInstance();
        JSONObject json = readJsonObject();
        config.populateMap("", json.toMap());
        return config;
    }
}
