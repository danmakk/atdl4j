package org.atdl4j.ui.javafx.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.validation.Field2OperatorValidationRule;
import org.atdl4j.data.validation.LogicalOperatorValidationRule;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.AbstractStrategyUI;
import org.atdl4j.ui.javafx.JavaFXWidget;
import org.atdl4j.ui.javafx.widget.JavaFXButtonWidget;

/**
 * @author daniel.makgonta
 */
public class JavaFXStrategyUI extends AbstractStrategyUI {

    protected static final Logger logger = Logger.getLogger(JavaFXStrategyUI.class);

    protected Map<String, JavaFXWidget<?>> javafxWidgetMap;
    protected Map<String, JavaFXWidget<?>> javafxWidgetWithParameterMap;

    private Pane parentComponent;

    protected List<JavaFXStateListener> stateListenerList;
    protected Map<JavaFXWidget<?>, Set<JavaFXStateListener>> widgetStateListenerMap;
    protected Map<String, ToggleGroup> radioGroupMap;
    private JavaFXStrategyPanelFactory strategyPanelFactory = new JavaFXStrategyPanelFactory();

    @Override
    protected void buildAtdl4jWidgetMap(List<StrategyPanelT> aStrategyPanelList) throws FIXatdlFormatException {
        VBox childComponent = new VBox();
        parentComponent = new AnchorPane();
        int row = 0;
        VBox tmp = new VBox();
        Map<String, JavaFXWidget<?>> tempJavaFXWidgetMap = new HashMap<String, JavaFXWidget< ?>>();
        // build panels and widgets recursively
        for (StrategyPanelT panel : aStrategyPanelList) {
            Map<String, JavaFXWidget< ?>> strategyPanelAndWidgets = strategyPanelFactory
                    .createStrategyPanelAndWidgets(tmp, panel,
                            getParameterMap(), 0,
                            getAtdl4jWidgetFactory());
            tempJavaFXWidgetMap.putAll(strategyPanelAndWidgets);
            Pane container = strategyPanelFactory.createStrategyPanel(new VBox(), panel, 0, strategyPanelAndWidgets, 0);
            childComponent.getChildren().add(container);
            row++;
        }

        parentComponent.getChildren().add(childComponent);
        javafxWidgetMap = tempJavaFXWidgetMap;

        //show(parentComponent);
        setAllVisible(parentComponent);
    }

    public Pane getParentComponent() {
        return parentComponent;
    }

    private void setAllVisible(Node node) {
        if (node != null) {
            node.setVisible(true);
            if (node instanceof Pane) {
                Pane p = (Pane) node;
                for (int i = 0; i < p.getChildren().size(); i++) {
                    setAllVisible(p.getChildren().get(i));
                }
            }
        }
    }

    @Override
    public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetMap() {
        if (javafxWidgetMap != null) {
            return new HashMap<String, Atdl4jWidget<?>>(javafxWidgetMap);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Atdl4jWidget<?>> getAtdl4jWidgetWithParameterMap() {
        if (javafxWidgetWithParameterMap != null) {
            return new HashMap<String, Atdl4jWidget<?>>(javafxWidgetWithParameterMap);
        } else {
            return null;
        }
    }

    @Override
    protected void initBegin(Object parentContainer) {
        parentComponent = (Pane) parentContainer;
        javafxWidgetWithParameterMap = new HashMap<String, JavaFXWidget<?>>();
        widgetStateListenerMap = new HashMap<JavaFXWidget<?>, Set<JavaFXStateListener>>();
    }

    @Override
    protected void buildAtdl4jWidgetMap() throws FIXatdlFormatException {
        if (getStrategy() == null) {
            throw new FIXatdlFormatException("getStrategy() was null.");
        }

        if (getStrategy().getStrategyLayout() == null) {
            throw new FIXatdlFormatException("getStrategy().getStrategyLayout() was null (verify <lay:StrategyLayout>)");
        }

        if (getStrategy().getStrategyLayout() == null) {
            throw new FIXatdlFormatException("getStrategy().getStrategyLayout().getStrategyPanel() was null (verify <lay:StrategyLayout> <lay:StrategyPanel>)");
        }

        buildAtdl4jWidgetMap(getStrategy().getStrategyLayout().getStrategyPanel());

        setAllVisible(parentComponent);
    }

    @Override
    protected void createRadioGroups() {
        Map<String, ToggleGroup> tempRadioGroupMap = new HashMap<String, ToggleGroup>();

        for (JavaFXWidget<?> widget : javafxWidgetMap.values()) {
            if (widget.getControl() instanceof RadioButtonT && ((RadioButtonT) widget.getControl()).getRadioGroup() != null
                    && !"".equals(((RadioButtonT) widget.getControl()).getRadioGroup())) {
                String rg = ((RadioButtonT) widget.getControl()).getRadioGroup();
                if (!tempRadioGroupMap.containsKey(rg)) {
                    tempRadioGroupMap.put(rg, new ToggleGroup());
                }

                if (widget instanceof JavaFXButtonWidget) {
                    tempRadioGroupMap.get(rg).getToggles().add(((JavaFXButtonWidget)widget).getRadioButton());
                }
            }
        }
        radioGroupMap = tempRadioGroupMap;
    }

    @Override
    protected void buildAtdl4jWidgetWithParameterMap() {
        Map<String, JavaFXWidget<?>> tempJavaFXWidgetWithParameterMap = new HashMap<String, JavaFXWidget<?>>();
        // loop through all UI controls
        for (JavaFXWidget<?> widget : javafxWidgetMap.values()) {
            if (widget.getParameter() != null) {
                // validate that a parameter is not being added twice
                String tempParameterName = widget.getParameter().getName();
                if (tempJavaFXWidgetWithParameterMap.containsKey(tempParameterName)) {
                    throw new IllegalStateException("Cannot add parameter \"" + tempParameterName + "\" to two separate controls.");
                }
                tempJavaFXWidgetWithParameterMap.put(tempParameterName, widget);
            }
        }
        javafxWidgetWithParameterMap = tempJavaFXWidgetWithParameterMap;
    }

    @Override
    protected void attachGlobalStateRulesToControls() throws FIXatdlFormatException {
        List<JavaFXStateListener> tempStateListenerList = new Vector<JavaFXStateListener>();

        // loop through all UI controls
        for (JavaFXWidget<?> widget : javafxWidgetMap.values()) {
            // parameter state rules that have an id should be included in
            // the rules map
            ControlT control = widget.getControl();

            if (control.getStateRule() != null) {
                for (StateRuleT stateRule : control.getStateRule()) {

                    JavaFXWidget<?> affectedWidget = javafxWidgetMap.get(control.getID());
                    JavaFXStateListener stateListener = new JavaFXStateListener(affectedWidget, stateRule, javafxWidgetMap, getCompleteValidationRuleMap());

                    // attach the stateListener's rule to controls
                    attachRuleToControls(stateListener.getRule(), stateListener);

                    tempStateListenerList.add(stateListener);

                    // run the state rule to check if any parameter needs to be
                    // enabled/disabled or hidden/unhidden before being rendered
                    stateListener.handleEvent();
                }
            }
        }
        stateListenerList = tempStateListenerList;
    }

    private void attachRuleToControls(ValidationRule rule, JavaFXStateListener stateRuleListener) {
        if (rule instanceof LogicalOperatorValidationRule) {
            for (ValidationRule innerRule : ((LogicalOperatorValidationRule) rule).getRules()) {
                attachRuleToControls(innerRule, stateRuleListener);
            }
        } else if (rule instanceof ValueOperatorValidationRule) {
            attachFieldToControls(((ValueOperatorValidationRule) rule).getField(), stateRuleListener);
        } else if (rule instanceof Field2OperatorValidationRule) {
            attachFieldToControls(((Field2OperatorValidationRule) rule).getField(), stateRuleListener);
            attachFieldToControls(((Field2OperatorValidationRule) rule).getField2(), stateRuleListener);
        }
    }

    private void attachFieldToControls(String field, JavaFXStateListener stateRuleListener) {
        if (field != null) {
            JavaFXWidget<?> targetParameterWidget = javafxWidgetMap.get(field);
            if (targetParameterWidget == null) {
                throw new IllegalStateException("Error generating a State Rule => Control: " + field + " does not exist in Strategy: " + getStrategy().getName());
            }
            putStateListener(targetParameterWidget, stateRuleListener);

            // -- RadioButtonT requires adding all associated RadioButtonTs in the radioGroup --
            if (targetParameterWidget.getControl() instanceof RadioButtonT
                    && ((RadioButtonT) targetParameterWidget.getControl()).getRadioGroup() != null
                    && (!"".equals(((RadioButtonT) targetParameterWidget.getControl()).getRadioGroup()))) {
                String rg = ((RadioButtonT) targetParameterWidget.getControl()).getRadioGroup();
                for (JavaFXWidget<?> widget : javafxWidgetMap.values()) {
                    if (widget.getControl() instanceof RadioButtonT && ((RadioButtonT) widget.getControl()).getRadioGroup() != null
                            && ((RadioButtonT) widget.getControl()).getRadioGroup() != null
                            && (!"".equals(((RadioButtonT) widget.getControl()).getRadioGroup()))
                            && ((RadioButtonT) widget.getControl()).getRadioGroup().equals(rg)) {
                        putStateListener(widget, stateRuleListener);
                    }
                }
            }
        }
    }

    private void putStateListener(JavaFXWidget<?> widget, JavaFXStateListener stateListener) {
        if (!widgetStateListenerMap.containsKey(widget)) {
            widgetStateListenerMap.put(widget, new HashSet<JavaFXStateListener>());
        }
        if (!widgetStateListenerMap.get(widget).contains(stateListener)) {
            widgetStateListenerMap.get(widget).add(stateListener);
        }
    }

    @Override
    protected void attachStateListenersToAllAtdl4jWidgets() {
        for (Entry<JavaFXWidget<?>, Set<JavaFXStateListener>> entry : widgetStateListenerMap.entrySet()) {
            for (JavaFXStateListener listener : entry.getValue()) {
                entry.getKey().addListener(listener);
            }
        }
    }

    @Override
    protected void initEnd() {
    }

    @Override
    protected void addToAtdl4jWidgetMap(String aName, Atdl4jWidget<?> aAtdl4jWidget) {
        javafxWidgetMap.put(aName, (JavaFXWidget<?>) aAtdl4jWidget);
    }

    @Override
    protected void addToAtdl4jWidgetWithParameterMap(String aName, Atdl4jWidget<?> aAtdl4jWidget) {
        javafxWidgetWithParameterMap.put(aName, (JavaFXWidget<?>) aAtdl4jWidget);
    }

    @Override
    protected void removeFromAtdl4jWidgetMap(String aName) {
        javafxWidgetMap.remove(aName);
    }

    @Override
    protected void removeFromAtdl4jWidgetWithParameterMap(String aName) {
        javafxWidgetWithParameterMap.remove(aName);
    }

    @Override
    public void setCxlReplaceMode(boolean cxlReplaceMode) {
        for (JavaFXWidget<?> widget : javafxWidgetWithParameterMap.values()) {
            if (!widget.getParameter().isMutableOnCxlRpl()) {
                widget.setEnabled(!cxlReplaceMode);
            }
        }

        // set all CxlRpl on all state listeners and fire
        // once for good measure
        for (JavaFXStateListener stateListener : stateListenerList) {
            stateListener.setCxlReplaceMode(cxlReplaceMode);
            stateListener.handleEvent();
        }
    }

    @Override
    protected void fireStateListeners() {
        for (JavaFXStateListener stateListener : stateListenerList) {
            stateListener.handleEvent();
        }
    }

    @Override
    protected void fireStateListenersForAtdl4jWidget(Atdl4jWidget<?> aAtdl4jWidget) {
        for (JavaFXStateListener stateListener : stateListenerList) {
            if (aAtdl4jWidget.equals(stateListener.getAffectedWidget())) {
                stateListener.handleEvent();
            }
        }
    }

    @Override
    protected void fireLoadFixMessageStateListenersForAtdl4jWidget(Atdl4jWidget<?> aAtdl4jWidget) {
        for (JavaFXStateListener stateListener : stateListenerList) {
            if (aAtdl4jWidget.equals(stateListener.getAffectedWidget())) {
                stateListener.handleLoadFixMessageEvent();
            }
        }
    }

    @Override
    protected void applyRadioGroupRules() {
        if (radioGroupMap != null) {
            for (ToggleGroup tempJavaFXRadioButtonListener : radioGroupMap.values()) {
                // -- If no RadioButtons within the radioGroup are selected, then first one in list will be selected --
                RadioButton selectedItem = (RadioButton) tempJavaFXRadioButtonListener.getSelectedToggle();

                if (selectedItem == null) {
                    selectedItem = (RadioButton) tempJavaFXRadioButtonListener.getToggles().get(0);
                    selectedItem.setSelected(true);
                }
            }
        }
    }

    @Override
    public void relayoutCollapsibleStrategyPanels() {
    }

}
