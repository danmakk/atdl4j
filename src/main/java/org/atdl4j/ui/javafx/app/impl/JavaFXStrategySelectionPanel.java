package org.atdl4j.ui.javafx.app.impl;

import java.util.List;
import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.app.impl.AbstractStrategySelectionPanel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class JavaFXStrategySelectionPanel extends AbstractStrategySelectionPanel {

    private final Logger logger = Logger.getLogger(JavaFXStrategySelectionPanel.class);

    private ComboBox<String> strategiesDropDown;
    private StrategyT strategy;

    @Override
    public Object buildStrategySelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions) {
        return buildStrategySelectionPanel((Parent) parentOrShell, atdl4jOptions);
    }

    public Pane buildStrategySelectionPanel(Parent aParentContainer, Atdl4jOptions atdl4jOptions) {
        setAtdl4jOptions(atdl4jOptions);

        BorderPane panel = new BorderPane();
        // label
        Label strategiesDropDownLabel = new Label("Strategy");
        panel.setLeft(strategiesDropDownLabel);

        // dropDownList
        strategiesDropDown = new ComboBox<String>();
        strategiesDropDown.setEditable(false);

        panel.setCenter(strategiesDropDown);

        if (Atdl4jConfig.getConfig().getStrategyDropDownItemDepth() != null) {
            strategiesDropDown.getSelectionModel().select(0);
        }
        // tooltip
        strategiesDropDown.setTooltip(new Tooltip("Select A Strategy"));

        // action listener
        strategiesDropDown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String oldVal, String newVal) {
                firePreStrategySelectedEvent();
                int index = strategiesDropDown.getSelectionModel().getSelectedIndex();
                selectDropDownStrategy(index);
            }
        });
        
        return panel;
    }

    @Override
    public void loadStrategyList(List<StrategyT> aStrategyList) {

        final List<StrategyT> tmpStratList = aStrategyList;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                strategiesDropDown.getSelectionModel().clearSelection();

                setStrategiesList(tmpStratList);

                if (getStrategiesList() == null) {
                    return;
                }

                for (StrategyT tempStrategy : getStrategiesList()) {
                    strategiesDropDown.getSelectionModel().clearSelection();
                    logger.debug("loadStrategyList() [" + strategiesDropDown.getItems().size() + "] strategiesDropDown.add: " + Atdl4jHelper.getStrategyUiRepOrName(tempStrategy));
                    strategiesDropDown.getItems().add(Atdl4jHelper.getStrategyUiRepOrName(tempStrategy));
                }
            }
        });
    }

    public void selectDropDownStrategy(int index) {
        logger.debug("selectDropDownStrategy() index: " + index);

        if (getStrategiesList().size() != strategiesDropDown.getItems().size()) {
            return;
        }

        strategiesDropDown.getSelectionModel().select(index);

        StrategyT tempStrategy = getStrategiesList().get(index);

        if (strategiesDropDown.getItems().get(index) != null) {
            if (!strategiesDropDown.getItems().get(index).equals(Atdl4jHelper.getStrategyUiRepOrName(tempStrategy))) {
                throw new IllegalStateException("UNEXPECTED ERROR: strategiesDropDown.getItem(" + index + "): "
                        + strategiesDropDown.getItems().get(index) + " DID NOT MATCH tempStrategy: "
                        + Atdl4jHelper.getStrategyUiRepOrName(tempStrategy));
            }
        }

        fireStrategySelectedEvent(tempStrategy);
    }

    // 4/16/2010 Scott Atwell public void selectDropDownStrategy(String
    // strategyName)
    public void selectDropDownStrategyByStrategyName(String aStrategyName) {
        logger.debug("selectDropDownStrategyByStrategyName() aStrategyName: " + aStrategyName);

        if (getStrategiesList().size() != strategiesDropDown.getItems().size()) {
            throw new IllegalStateException("UNEXPECTED ERROR: getStrategiesList().size(): "
                    + getStrategiesList().size() + " does NOT MATCH strategiesDropDown.getItemCount(): "
                    + strategiesDropDown.getItems().size());
        }

        for (int i = 0; i < getStrategiesList().size(); i++) {
            StrategyT tempStrategy = getStrategiesList().get(i);

            if (aStrategyName.equals(tempStrategy.getName())) {
                logger.debug("selectDropDownStrategyByStrategyName() invoking selectDropDownStrategy( " + i + " )");
                selectDropDownStrategy(i);
            }
        }
    }

    // 4/16/2010 Scott Atwell added
    public void selectDropDownStrategyByStrategyWireValue(String aStrategyWireValue) {
        logger.debug("selectDropDownStrategyByStrategyWireValue() aStrategyWireValue: " + aStrategyWireValue);

        if (getStrategiesList().size() != strategiesDropDown.getItems().size()) {
            throw new IllegalStateException("UNEXPECTED ERROR: getStrategiesList().size(): "
                    + getStrategiesList().size() + " does NOT MATCH strategiesDropDown.getItemCount(): "
                    + strategiesDropDown.getItems().size());
        }

        for (int i = 0; i < getStrategiesList().size(); i++) {
            StrategyT tempStrategy = getStrategiesList().get(i);

            if (aStrategyWireValue.equals(tempStrategy.getWireValue())) {
                logger.debug(
                        "selectDropDownStrategyByStrategyWireValue() invoking selectDropDownStrategy( " + i + " )");
                selectDropDownStrategy(i);
            }
        }
    }

    public void selectFirstDropDownStrategy() {
        if ((strategiesDropDown != null) && (strategiesDropDown.getItems().size() > 0)) {
            strategiesDropDown.getSelectionModel().select(0);
            logger.debug("selectFirstDropDownStrategy() invoking selectDropDownStrategy( 0 )");
            selectDropDownStrategy(0);
        }
    }

    public void setEnabled(boolean enable) {
        strategiesDropDown.setDisable(!enable);
    }
}
