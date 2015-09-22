/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public static String[] args;

    public static void main(String[] args) {
        JavaFXDisplayPanel.args = args;
        launch();
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Atdl4jConfiguration config = new JavaFXAtdl4jConfiguration();
        JavaFXAtdl4jTesterApp tempJavaFXAtdl4jTesterApp = new JavaFXAtdl4jTesterApp();
        try {
            Pane root = tempJavaFXAtdl4jTesterApp.mainLine(args, config);
            
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
