package org.atdl4j.ui.javafx.app.impl;


import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.ui.javafx.config.JavaFXAtdl4jConfiguration;

public class JavaFXStrategyBuilder {

    private final String XMLFilePath;
    private ArrayList<? extends Node> strategyWidgets;
    
    public JavaFXStrategyBuilder(String XMLFilePath) {
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
            this.strategyWidgets = tempJavaFXAtdl4jTesterApp.getStrategyWidgets();
        } catch (Exception e) {
            if (Atdl4jConfig.getConfig().isCatchAllMainlineExceptions()) {
                tempJavaFXAtdl4jTesterApp.logger.warn("Fatal Exception in mainLine", e);
            } else {
                throw e;
            }
        }
        
        return root;
    }
    
    public ArrayList<? extends Node> getStrategyWidgets(){
        return strategyWidgets; 
    }
}
