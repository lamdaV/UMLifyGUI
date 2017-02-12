package controller;

import app.UMLEngine;
import config.IConfiguration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import runner.RunnerConfiguration;
import parser.ConfigParser;
import viewRunner.ViewerRunner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

public class MasterController {
    private ViewerRunner viewRunner;
    private ConfigParser configParser;

    @FXML
    private TextField jarPath;
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
        this.configParser = new ConfigParser();
        this.viewRunner = new ViewerRunner(this.viewer.getEngine());
    }

    @FXML
    protected void addJar(ActionEvent event) {
        try {
            String path = this.jarPath.getText();
            log("jarPath: " + path);
            addPath(path);
            this.jarPath.clear();
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.appendText(e.toString() + "\n");
        }
    }

    /**
     * Source: https://stackoverflow.com/questions/7884393/can-a-directory-be-added-to-the-class-path-at-runtime
     *
     * @param s
     * @throws Exception
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
            log("configPath: " + path);
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
            log("starting UMLify");
            IConfiguration config = this.configParser.create();
            Method getInstanceMethod = UMLEngine.class.getDeclaredMethod("getInstance", IConfiguration.class);
            getInstanceMethod.setAccessible(true); //if security settings allow this
            Runnable engine = (Runnable) getInstanceMethod.invoke(null, config); //use null if the method is static
            engine.run();

            log("loading image to viewer");
            this.viewRunner.setConfig(config.createConfiguration(RunnerConfiguration.class));
            viewRunner.run();
            this.tabPane.getSelectionModel().select(this.viewerTab);
        } catch (Exception e) {
            e.printStackTrace();
            log(e.toString());
        }
    }

    private void log(String text) {
        this.logger.appendText(text + "\n");
    }
}
