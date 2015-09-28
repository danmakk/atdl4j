/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.app.impl;

import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jConfiguration;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.impl.AbstractAtdl4jTesterApp;
import org.atdl4j.ui.javafx.config.JavaFXAtdl4jConfiguration;
import org.atdl4j.ui.javafx.impl.JavaFXStrategiesUI;
import org.atdl4j.ui.javafx.impl.JavaFXStrategyUI;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXAtdl4jTesterApp extends AbstractAtdl4jTesterApp {

    public static final Logger logger = Logger.getLogger(JavaFXAtdl4jTesterApp.class);

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Atdl4jConfiguration config = new JavaFXAtdl4jConfiguration();
        JavaFXAtdl4jTesterApp tempJavaFXAtdl4jTesterApp = new JavaFXAtdl4jTesterApp();
        try {
            tempJavaFXAtdl4jTesterApp.mainLine(config, "/org.atdl4j.examples/sample1.xml");
        } catch (Exception e) {
            if (Atdl4jConfig.getConfig().isCatchAllMainlineExceptions()) {
                JavaFXAtdl4jTesterApp.logger.warn("Fatal Exception in mainLine", e);
            } else {
                throw e;
            }
        }
    }

    public Pane mainLine(Atdl4jConfiguration config, String XMLFilePath) throws Exception {
        AnchorPane pane = new AnchorPane();
        AnchorPane root = new AnchorPane();

        ComboBox<StrategyT> strategyList = new ComboBox<StrategyT>();
        strategyList.setMinWidth(120);

        HBox containerBox = new HBox();
        containerBox.setPadding(new Insets(5, 10, 5, 5));
        final VBox strategyBox = new VBox();
        root.getChildren().add(containerBox);

        // -- Delegate setup to AbstractAtdl4jTesterApp, construct a new
        // JavaFX-specific Atdl4jOptions --
        Atdl4jOptions atdl4jOptions = new Atdl4jOptions();
        atdl4jOptions.setShowEnabledCheckboxOnOptionalClockControl(true);
        init(config, atdl4jOptions, pane);

        // -- Build the JavaFX panel from Atdl4jTesterPanel (** core GUI component **) --
        getAtdl4jTesterPanel().buildAtdl4jTesterPanel(pane, getAtdl4jOptions());
        Atdl4jCompositePanel panel = getAtdl4jTesterPanel().getAtdl4jCompositePanel();
        panel.parseFixatdlFile(XMLFilePath);
        panel.loadScreenWithFilteredStrategies();
        final JavaFXStrategiesUI uiList = (JavaFXStrategiesUI) panel.getStrategiesUI();
        List<StrategyT> strategies = panel.getStrategiesFilteredStrategyList();

        strategyList.getItems().addAll(strategies);
        strategyList.setCellFactory(new Callback<ListView<StrategyT>, ListCell<StrategyT>>() {
            @Override
            public ListCell<StrategyT> call(ListView<StrategyT> param) {
                return new StrategyListCell();
            }
        });

        strategyList.setConverter(new StringConverter<StrategyT>() {

            @Override
            public String toString(StrategyT strategy) {
                if (strategy != null) {
                    return strategy.getUiRep();
                }
                return null;
            }

            @Override
            public StrategyT fromString(String string) {
                return null;
            }
        });

        strategyList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StrategyT>() {

            @Override
            public void changed(ObservableValue<? extends StrategyT> observable, StrategyT oldValue, StrategyT newValue) {
                strategyBox.getChildren().clear();

                JavaFXStrategyUI ui = (JavaFXStrategyUI) uiList.getStrategyUI(newValue, true);

                strategyBox.getChildren().add(ui.getParentComponent());

            }
        });

        pane.setPrefSize(600, 600);
        root.setPrefSize(700, 700);

        containerBox.getChildren().addAll(strategyList, strategyBox);

        return root;
    }

    private String[] convertToStringArray(List<StrategyT> strategy) {
        if (strategy.size() > 0) {
            String[] tmp = new String[strategy.size()];
            int i = 0;
            for (StrategyT strat : strategy) {
                String name = strat.getUiRep();
                tmp[i] = name;
                i++;
            }
            return tmp;
        }
        return null;
    }

    class StrategyListCell extends ListCell<StrategyT> {

        @Override
        protected void updateItem(StrategyT item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getUiRep());
            }
        }
    }
}
