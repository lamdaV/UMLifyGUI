package parser;

import config.IConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lamd on 2/16/2017.
 */
public class ConfigurationParser implements IConfigurationParser {
    private String configPath;
    private Map<String, IParser> parserMap;

    public ConfigurationParser() {
        this.configPath = "";
        this.parserMap = new HashMap<>();
    }

    @Override
    public void setConfigPath(String path) {
        this.configPath = path;
    }

    @Override
    public IConfiguration create() throws Exception {
        String extension = this.configPath.substring(this.configPath.lastIndexOf(".") + 1);
        IParser parser = this.parserMap.get(extension);
        parser.setConfigPath(this.configPath);
        return parser.create();
    }

    @Override
    public void addParser(String extension, IParser parser) {
        this.parserMap.put(extension, parser);
    }
}
