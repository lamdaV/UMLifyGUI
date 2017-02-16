package parser;

/**
 * Created by lamd on 2/16/2017.
 */
public interface IConfigurationParser extends IParser {
    void addParser(String key, IParser parser);
}
