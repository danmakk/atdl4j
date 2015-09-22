package org.atdl4j.ui.javafx.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.StrategiesUI;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.StrategySelectionEvent;
import org.atdl4j.ui.app.impl.AbstractAtdl4jCompositePanel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class JavaFXAtdl4jCompositePanel extends AbstractAtdl4jCompositePanel {

    public final Logger logger = Logger.getLogger(JavaFXAtdl4jCompositePanel.class);
    private Parent parentComposite;
    private Pane strPanel;
    private Pane strategySelectionPanel;
    private Pane panel;

    @Override
    public void init(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) {
        super.init(aParentOrShell, aAtdl4jOptions);
    }

    @Override
    public Object buildAtdl4jCompositePanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) {
        return buildAtdl4jCompositePanel((Pane) aParentOrShell, aAtdl4jOptions);
    }

    public Parent buildAtdl4jCompositePanel(Pane aParentComposite, Atdl4jOptions aAtdl4jOptions) {
        parentComposite = aParentComposite;

        panel = new VBox();
        panel.setPadding(new Insets(3, 3, 8, 3));
        // -- Delegate back to AbstractAtdl4jCompositePanel --
        init(parentComposite, aAtdl4jOptions);

        TilePane tile = new TilePane();
        tile.setOrientation(Orientation.VERTICAL);

        strategySelectionPanel = tile;
        strategySelectionPanel.getChildren().add((BorderPane) getStrategySelectionPanel().buildStrategySelectionPanel(getParentOrShell(),
                getAtdl4jOptions()));
        panel.getChildren().add(strategySelectionPanel);

        strPanel = (Pane) getStrategiesUI().buildStrategiesPanel(getParentOrShell(), getAtdl4jOptions(),
                getAtdl4jUserMessageHandler());
        panel.getChildren().add(strPanel);

        VBox descrPanel = (VBox) getStrategyDescriptionPanel()
                .buildStrategyDescriptionPanel(getParentOrShell(), getAtdl4jOptions());

        panel.getChildren().add(descrPanel);
        descrPanel.setVisible(false);

        panel.getChildren().add(new VBox());

        return panel;
    }

    public void setVisibleStrategySectionPanel(boolean aVisible) {
        if (strategySelectionPanel != null) {
            strategySelectionPanel.setVisible(aVisible);
        }
    }

    @Override
    protected void packLayout() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (strPanel != null) {
                    strPanel.autosize();
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.impl.AbstractAtdl4jCompositePanel#
     * setVisibleOkCancelButtonSection(boolean)
     */
    @Override
    public void setVisibleOkCancelButtonSection(boolean aVisible) {

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.atdl4j.ui.app.StrategySelectionPanelListener#strategySelected(org.
     * atdl4j.fixatdl.core.StrategyT, int)
     */
    @Override
    public void strategySelected(StrategyT aStrategy) {
        setSelectedStrategy(aStrategy);
        setSelectedStrategyValidated(false);
        getStrategyDescriptionPanel().loadStrategyDescriptionVisible(aStrategy);
        getStrategiesUI().adjustLayoutForSelectedStrategy(aStrategy);
        getStrategyDescriptionPanel().loadStrategyDescriptionText(aStrategy);
        // -- Notify StrategyEventListener (eg Atdl4jTesterPanel),
        // aSelectedViaLoadFixMsg=false --
        fireStrategyEventListenerStrategySelected(aStrategy, false);
        packLayout();
    }

    @Override
    public void beforeStrategyIsSelected(StrategySelectionEvent event) {
        fireStrategyEventListenerBeforeStrategySelected();
    }

    @Override
    public void setEditable(boolean b) {
        setEnabled(b);
    }

    public void setEnabled(boolean enable) {
        setStrategySelectionEnabled(enable);
        StrategiesUI strategiesUI = getStrategiesUI();
        StrategyT selectedStrategy = getSelectedStrategy();
        if (selectedStrategy != null) {
            StrategyUI ui = strategiesUI.getStrategyUI(selectedStrategy, false);
            if (ui != null) {
                for (Atdl4jWidget<?> widget : ui.getAtdl4jWidgetMap().values()) {
                    widget.setEnabled(enable);
                }
            }
        }
    }

    public void setStrategySelectionEnabled(boolean enable) {
        ((JavaFXStrategySelectionPanel) getStrategySelectionPanel()).setEnabled(enable);
    }
}
