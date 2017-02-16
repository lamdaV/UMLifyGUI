package viewRunner;

import javafx.scene.web.WebEngine;
import runner.RunnerConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Views the outputted file.
 * <p>
 * Created by lamd on 1/11/2017.
 */
public class ViewerRunner implements Runnable {
    private final WebEngine engine;
    private RunnerConfiguration config;

    public ViewerRunner(WebEngine engine) {
        this(null, engine);
    }

    /**
     * Constructs a ViewerRunner.
     *
     * @param config IRunnerConfiguration config.
     */
    public ViewerRunner(RunnerConfiguration config, WebEngine engine) {
        setConfig(config);
        this.engine = engine;
    }

    public void setConfig(RunnerConfiguration config) {
        this.config = config;
    }

    /**
     * Run the default OS specific browser.
     */
    public void run() {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        currentPath = currentPath.resolve(this.config.getOutputDirectory());
        currentPath = currentPath.resolve(String.format("%s.%s", this.config.getFileName(), this.config.getOutputFormat()));
        currentPath = currentPath.normalize();

        this.engine.load("file:/" + currentPath);

    }
}
