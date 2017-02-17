package parser;

/**
 * A ConfigurationParser Interface.
 * It defines a ConfigurationParser an add method for the user to dynamically add new parser at runtime.
 * <p>
 * Created by lamd on 2/16/2017.
 */
public interface IConfigurationParser extends IParser {
    void addParser(String key, IParser parser);
}
