package parser;

import config.ConfigurationFactory;

public interface IConfigurationParser extends ConfigurationFactory{
	void setConfigPath(String path);
}
