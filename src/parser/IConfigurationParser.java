package parser;

import config.ConfigurationFactory;

/**
 * A ConfigurationParser Interface for controller use.
 *
 */
public interface IConfigurationParser extends ConfigurationFactory {
    /**
     * Sets the file path of where the configuration file is located.
     *
     * @param path String path of the configuration file.
     */
    void setConfigPath(String path);
}
