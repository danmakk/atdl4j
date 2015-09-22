package org.atdl4j.ui.javafx.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.fixatdl.core.StrategiesT;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.fixatdl.validation.EditT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.StrategyUI;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.impl.AbstractStrategiesUI;
import org.atdl4j.ui.javafx.JavaFXListener;
import org.atdl4j.ui.javafx.JavaFXWidget;

/**
 * @author daniel.makgonta
 */
public class JavaFXStrategiesUI extends AbstractStrategiesUI {

    private Parent parentFrame;
    private Pane strategiesPanel;
    private Map<String, JavaFXWidgetListener> javafxWidgetListeners;
    private int pendingNotifications;

    public JavaFXStrategiesUI() {
        this.javafxWidgetListeners = new HashMap<String, JavaFXWidgetListener>();
    }

    public JavaFXStrategiesUI(Atdl4jOptions aAtdl4jOptions) {
        init(aAtdl4jOptions);
    }

    @Override
    public void init(Atdl4jOptions aAtdl4jOptions) {
        setAtdl4jOptions(aAtdl4jOptions);
    }

    @Override
    public Object buildStrategiesPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler) {
        return buildStrategiesPanel((Pane) parentOrShell, atdl4jOptions, aAtdl4jUserMessageHandler);
    }

    public Pane buildStrategiesPanel(Pane aParentComposite, Atdl4jOptions atdl4jOptions, Atdl4jUserMessageHandler aAtdl4jUserMessageHandler) {
        parentFrame = aParentComposite;

        setAtdl4jOptions(atdl4jOptions);

        setAtdl4jUserMessageHandler(aAtdl4jUserMessageHandler);

        // Main strategies panel
        strategiesPanel = new AnchorPane();

        return strategiesPanel;
    }

    @Override
    public void createStrategyPanels(StrategiesT aStrategies, List<StrategyT> aFilteredStrategyList) throws FIXatdlFormatException {
        // -- Check to see if StrategiesT has changed (eg new file loaded) --
        if ((getStrategies() == null) || (!getStrategies().equals(aStrategies))) {
            setStrategies(aStrategies);

            setStrategiesRules(new HashMap<String, ValidationRule>());
            for (EditT edit : getStrategies().getEdit()) {
                String id = edit.getId();
                if (id != null) {
                    ValidationRule rule = ValidationRuleFactory.createRule(edit,
                            getStrategiesRules(), getStrategies());
                    getStrategiesRules().put(id, rule);
                } else {
                    throw new IllegalArgumentException("Strategies-scoped edit without id");
                }
            }
        }

        setPreCached(false);
        setCurrentlyDisplayedStrategyUI(null);
        if (getAtdl4jOptions().isPreloadPanels()) {
            for (StrategyT strategy : aFilteredStrategyList) {
                removeAllStrategyPanels();
                StrategyUI ui = org.atdl4j.ui.javafx.impl.JavaFXStrategyUIFactory
                        .createStrategyUIAndContainer(this, strategy);
                setCurrentlyDisplayedStrategyUI(ui);
            }
            setPreCached(true);
        }
    }

    @Override
    public void removeAllStrategyPanels() {
        if (strategiesPanel != null) {
            if (strategiesPanel.getChildren().size() > 0) {
                strategiesPanel.getChildren().removeAll();
            }
        }
    }

    @Override
    public void adjustLayoutForSelectedStrategy(StrategyT aStrategy) {
        muteWidgetNotification();
        if (strategiesPanel != null) {
            // -- (aReinitPanelFlag=true) --
            StrategyUI tempStrategyUI = getStrategyUI(aStrategy, true);

            if (tempStrategyUI == null) {
                logger.info("ERROR:  Strategy name: " + aStrategy.getName() + " was not found.  (aStrategy: " + aStrategy + ")");
                return;
            }

            logger.debug("Invoking  tempStrategyUI.reinitStrategyPanel() for: " + Atdl4jHelper.getStrategyUiRepOrName(tempStrategyUI.getStrategy()));
            tempStrategyUI.reinitStrategyPanel();
        }
        allowWidgetNotification();
    }

    private void muteWidgetNotification() {
        pendingNotifications++;
    }

    private void allowWidgetNotification() {
        pendingNotifications--;
    }

    @Override
    public void setVisible(boolean aVisible) {
        if (strategiesPanel != null) {
            strategiesPanel.setVisible(aVisible);
        }
    }

    @Override
    public StrategyUI getStrategyUI(StrategyT aStrategy, boolean aReinitPanelFlag) {
        if (aStrategy.equals(getCurrentlyDisplayedStrategy())) {
            logger.debug("Strategy name: " + aStrategy.getName() + " is currently being displayed.  Returning getCurrentlyDisplayedStrategyUI()");
            // 12/15/2010 Scott Atwell return getCurrentlyDisplayedStrategyUI();
            if (aReinitPanelFlag) {
                getCurrentlyDisplayedStrategyUI().reinitStrategyPanel();
                setWidgetListeners(getCurrentlyDisplayedStrategyUI());
            }

            return getCurrentlyDisplayedStrategyUI();
        } else {
            logger.debug("Strategy name: " + aStrategy.getName() + " is not currently displayed.  Invoking removeAllStrategyPanels() and returning createStrategyPanel()");
            removeAllStrategyPanels();

            StrategyUI tempStrategyUI = JavaFXStrategyUIFactory.createStrategyUIAndContainer(this, aStrategy);
            setCurrentlyDisplayedStrategyUI(tempStrategyUI);

            logger.debug("Invoking relayoutCollapsibleStrategyPanels() for: " + aStrategy.getName());
            tempStrategyUI.relayoutCollapsibleStrategyPanels();
            setWidgetListeners(getCurrentlyDisplayedStrategyUI());

            return tempStrategyUI;
        }
    }

    private void setWidgetListeners(StrategyUI strategyUI) {
        for (JavaFXWidgetListener l : javafxWidgetListeners.values()) {
            l.dispose();
        }
        javafxWidgetListeners.clear();
        for (Atdl4jWidget<?> widget : strategyUI.getAtdl4jWidgetMap().values()) {
            // some widgets don't have a parameter reference, they are for control only (eg. radio buttons)
            if (widget.getParameter() != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding listener on "
                            + strategyUI.getStrategy().getName() + " "
                            + widget.getParameter().getName() + " widget " + widget);
                }
                javafxWidgetListeners.put(widget.getParameter().getName(), new JavaFXWidgetListener((JavaFXWidget) widget));
            }
        }
    }

    protected Pane getStrategiesPanel() {
        return this.strategiesPanel;
    }

    protected void setStrategiesPanel(Pane aStrategiesPanel) {
        this.strategiesPanel = aStrategiesPanel;
    }

    private boolean isWidgetNotificationsAllowed() {
        return pendingNotifications == 0;
    }

    public class JavaFXWidgetListener extends JavaFXListener {

        private String paramName;

        private final JavaFXWidget javafxWidget;

        public JavaFXWidgetListener(JavaFXWidget javafxWidget) {
            super();
            this.javafxWidget = javafxWidget;
            paramName = javafxWidget.getParameter().getName();
            javafxWidget.addListener(this);
        }

        public void dispose() {
            javafxWidget.removeListener(this);
        }

        private void fireWidgetChangedEvent(String aParamName) {
            if (logger.isDebugEnabled()) {
                logger.debug("Widget changed :" + aParamName);
            }
            if (isWidgetNotificationsAllowed()) {
                fireWidgetChanged(javafxWidget);
            }
        }

        @Override
        public void handleEvent() {
            fireWidgetChangedEvent(paramName);
        }

        @Override
        public JavaFXWidget<?> getAffectedWidget() {
            return javafxWidget;
        }

        @Override
        public ValidationRule getRule() {
            return null;
        }

        @Override
        public void setCxlReplaceMode(boolean flag) {
            // 
        }

        @Override
        public void handleLoadFixMessageEvent() {
            fireWidgetChangedEvent(paramName);
        }

        @Override
        public void handle(javafx.event.ActionEvent event) {
            fireWidgetChangedEvent(paramName);
        }

        @Override
        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            fireWidgetChangedEvent(paramName);
        }
    }
}
