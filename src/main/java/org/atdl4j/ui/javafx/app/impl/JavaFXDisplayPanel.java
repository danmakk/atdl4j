package org.atdl4j.ui.javafx.app.impl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.ui.javafx.config.JavaFXAtdl4jConfiguration;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXDisplayPanel extends Application {

    private final String xmlFilePath = "/org.atdl4j.examples/sample1.xml";

    public static void main(String[] args) {
        launch();
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Atdl4jConfiguration config = new JavaFXAtdl4jConfiguration();
        JavaFXAtdl4jTesterApp tempJavaFXAtdl4jTesterApp = new JavaFXAtdl4jTesterApp();
        try {
            Pane root = tempJavaFXAtdl4jTesterApp.mainLine(config, xmlFilePath);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            if (Atdl4jConfig.getConfig().isCatchAllMainlineExceptions()) {
                tempJavaFXAtdl4jTesterApp.logger.warn("Fatal Exception in mainLine", e);
            } else {
                throw e;
            }
        }
    }
}
