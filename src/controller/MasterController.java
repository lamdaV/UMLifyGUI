package controller;

import app.UMLEngine;
import config.IConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import parser.ConfigurationParser;
import parser.IConfigurationParser;
import parser.JSONParser;
import parser.PropertiesParser;
import runner.RunnerConfiguration;
import viewRunner.ViewerRunner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The controller of the fadgui fxml. It controls the flow of data between tabs.
 */
public class MasterController {
    private ViewerRunner viewRunner;
    private IConfigurationParser configParser;
    private FileChooser fileChooser;

    @FXML
    private TextField jarPath;

    @FXML
    private Button browseJar;
    @FXML
    private Button browseConfig;
    @FXML
    private TextField configPath;
    @FXML
    private TextArea logger;
    @FXML
    private WebView viewer;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab viewerTab;
    @FXML
    private Tab settingsTab;
    @FXML
    private Tab logTab;

    @FXML
    public void initialize() {
        this.configParser = new ConfigurationParser();
        this.configParser.addParser("json", new JSONParser());
        this.configParser.addParser("properties", new PropertiesParser());
        this.viewRunner = new ViewerRunner(this.viewer.getEngine());
        this.fileChooser = new FileChooser();
    }

    @FXML
    protected void addJar(ActionEvent event) {
        try {
            String path = this.jarPath.getText();
            log("[ INFO ] adding jarPath: " + path);
            addPath(path);
            this.jarPath.clear();
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.appendText(e.toString());
        }
    }

    /**
     * Add a given file at path s to the class path.
     * <p>
     * Source: https://stackoverflow.com/questions/7884393/can-a-directory-be-added-to-the-class-path-at-runtime
     *
     * @param s Path of the JAR to dynamically load.
     * @throws Exception If Class is unable to be loaded.
     */
    private void addPath(String s) throws Exception {
        File f = new File(s);
        URI u = f.toURI();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(urlClassLoader, u.toURL());
    }

    @FXML
    protected void addConfig(ActionEvent event) {
        try {
            String path = this.configPath.getText();
            log("[ INFO ] adding configPath: " + path);
            this.configParser.setConfigPath(path);
            this.configPath.clear();
        } catch (Exception e) {
            e.printStackTrace();
            log(e.toString());
        }
    }

    @FXML
    protected void start(ActionEvent actionEvent) {
        try {
            log("[ INFO ] Starting UMLify...");
            IConfiguration config = this.configParser.create();
            Method getInstanceMethod = UMLEngine.class.getDeclaredMethod("getInstance", IConfiguration.class);
            getInstanceMethod.setAccessible(true); //if security settings allow this
            Runnable engine = (Runnable) getInstanceMethod.invoke(null, config); //use null if the method is static
            engine.run();
            log("[ INFO ] UMLify completed");

            log("[ INFO ] Loading image to viewer");
            this.viewRunner.setConfig(config.createConfiguration(RunnerConfiguration.class));
            viewRunner.run();
            this.tabPane.getSelectionModel().select(this.viewerTab);
        } catch (Exception e) {
            e.printStackTrace();
            log(e.toString());
        }
    }

    @FXML
    protected void browseJar(ActionEvent event) {
        FileChooser.ExtensionFilter jarFilter = new FileChooser.ExtensionFilter("Jar file", "*.jar");
        File jarFile = browse(this.browseJar.getScene().getWindow(), jarFilter, "Browse for Jar");

        if (jarFile != null) {
            log("[ INFO ] adding jarPath: " + jarFile.toString());
            try {
                addPath(jarFile.toString());
            } catch (Exception e) {
                e.printStackTrace();
                log(e.toString());
            }
        }
    }

    @FXML
    protected void browseConfig(ActionEvent event) {
        FileChooser.ExtensionFilter configFilter = new FileChooser.ExtensionFilter("config extensions", "*.json", "*.JSON", "*.properties");
        File configFile = browse(this.browseConfig.getScene().getWindow(), configFilter, "Browse for Config");

        if (configFile != null) {
            log("[ INFO ] adding configPath: " + configFile.toString());
            this.configParser.setConfigPath(configFile.toString());
        }
    }

    private File browse(Window rootWindow, FileChooser.ExtensionFilter filter, String title) {
        this.fileChooser.getExtensionFilters().clear();
        this.fileChooser.getExtensionFilters().add(filter);
        this.fileChooser.setTitle(title);

        return this.fileChooser.showOpenDialog(rootWindow);
    }

    private void log(String text) {
        this.logger.appendText(text + "\n");
    }
}
