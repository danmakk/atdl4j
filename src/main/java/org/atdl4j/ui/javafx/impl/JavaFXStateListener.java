/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.impl;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.ValidationRule;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.data.validation.ValidationRuleFactory;
import org.atdl4j.data.validation.ValueOperatorValidationRule;
import org.atdl4j.fixatdl.flow.StateRuleT;
import org.atdl4j.fixatdl.validation.OperatorT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;
import org.atdl4j.ui.javafx.JavaFXWidget;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXStateListener extends JavaFXListener {

    private JavaFXWidget<?> affectedWidget;
    private StateRuleT stateRule;
    private Map<String, JavaFXWidget<?>> controls;
    private Map<String, ValidationRule> refRules;
    private ValidationRule rule;
    private boolean cxlReplaceMode = false;

    public JavaFXStateListener(JavaFXWidget<?> affectedWidget, StateRuleT stateRule, Map<String, JavaFXWidget<?>> controls, Map<String, ValidationRule> refRules) throws FIXatdlFormatException {
        this.affectedWidget = affectedWidget;
        this.stateRule = stateRule;
        this.controls = controls;
        this.refRules = refRules;
        if (stateRule.getEdit() != null) {
            this.rule = ValidationRuleFactory.createRule(stateRule.getEdit(), refRules, stateRule);
        }
        if (stateRule.getEditRef() != null) {
            this.rule = ValidationRuleFactory.createRule(stateRule.getEditRef());
        }
    }

    @Override
    public ValidationRule getRule() {
        return rule;
    }

    @Override
    public void handleEvent() {
        // Create a casted map so that Validatable<?> can be used
        Map<String, Atdl4jWidget<?>> targets = new HashMap<String, Atdl4jWidget<?>>(controls);

        try {
            rule.validate(refRules, targets);
        } catch (ValidationException e) {
            setBehaviorAsStateRule(false);
            return;
        }
        setBehaviorAsStateRule(true);
    }

    protected void setBehaviorAsStateRule(Boolean state) {

        // set visible
        if (stateRule.isVisible() != null) {
            try {
                affectedWidget.setVisible(!(stateRule.isVisible() ^ state));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // enabled and value setting rules are only valid if not in
        // Cancel Replace mode
        if (cxlReplaceMode && affectedWidget.getParameter() != null && !affectedWidget.getParameter().isMutableOnCxlRpl()) {
            affectedWidget.setEnabled(false);
        } else {
            // set enabled
            if (stateRule.isEnabled() != null) {
                affectedWidget.setEnabled(!(stateRule.isEnabled() ^ state));
            }

            // set value
            if (stateRule.getValue() != null) {
                if (state) {
                    String value = stateRule.getValue();
                    affectedWidget.setValueAsString(value);
                } //  -- state arg is false and value involved is VALUE_NULL_INDICATOR --
                else if (Atdl4jConstants.VALUE_NULL_INDICATOR.equals(stateRule.getValue())) {
                    // -- This resets the widget (other widgets than value="{NULL}") to non-null --
                    affectedWidget.setNullValue(Boolean.FALSE);
                }
            }
        }
    }

    @Override
    public void setCxlReplaceMode(boolean cxlReplaceMode) {
        this.cxlReplaceMode = cxlReplaceMode;
    }

    @Override
    public JavaFXWidget<?> getAffectedWidget() {
        return this.affectedWidget;
    }

    public boolean hasSetValueNullStateRule() {
        if (stateRule != null) {
            if (Atdl4jConstants.VALUE_NULL_INDICATOR.equals(stateRule.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleLoadFixMessageEvent() {
        if ((getAffectedWidget() != null)
                && (!getAffectedWidget().isNullValue())
                && (hasSetValueNullStateRule())) {

            if (getRule() instanceof ValueOperatorValidationRule) {
                ValueOperatorValidationRule tempValueOperatorValidationRule = (ValueOperatorValidationRule) getRule();
                Atdl4jWidget<?> tempAssociatedControl = controls.get(tempValueOperatorValidationRule.getField());

                if ((tempAssociatedControl != null)
                        && (ControlHelper.isControlToggleable(tempAssociatedControl.getControl()))) {
                    String tempRuleNewValueAsString = null;

                    if ("true".equals(tempValueOperatorValidationRule.getValue())) {
                        tempRuleNewValueAsString = "false";

                    } else if ("false".equals(tempValueOperatorValidationRule.getValue())) {
                        tempRuleNewValueAsString = "true";
                    }

                    if (tempRuleNewValueAsString != null) {
                        if ((OperatorT.EQ.equals(tempValueOperatorValidationRule.getOperator()))) {
                            tempAssociatedControl.setValueAsString(tempRuleNewValueAsString);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handle(ActionEvent event) {
        handleEvent();
    }

    @Override
    public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
        handleEvent();
    }
}
