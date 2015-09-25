package org.atdl4j.ui.javafx.app.impl;


import javafx.scene.layout.Pane;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.ui.javafx.config.JavaFXAtdl4jConfiguration;

public class StrategyBuilder {

    private final String XMLFilePath;
    
    public StrategyBuilder(String XMLFilePath) {
        this.XMLFilePath = XMLFilePath;
    }

    public Pane buildandGetStrategyPanel() throws Exception {
        if (XMLFilePath == null || "".equals(XMLFilePath)) {
            throw new Exception("Malformed string");
        }

        Pane root = null;

        Atdl4jConfiguration config = new JavaFXAtdl4jConfiguration();
        JavaFXAtdl4jTesterApp tempJavaFXAtdl4jTesterApp = new JavaFXAtdl4jTesterApp();
        
        try {
            root = tempJavaFXAtdl4jTesterApp.mainLine(config, XMLFilePath);
        } catch (Exception e) {
            if (Atdl4jConfig.getConfig().isCatchAllMainlineExceptions()) {
                tempJavaFXAtdl4jTesterApp.logger.warn("Fatal Exception in mainLine", e);
            } else {
                throw e;
            }
        }
        
        return root;
    }
}
