/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.CheckBoxListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXCheckBoxListWidget extends AbstractJavaFXWidget<String> {
    
    private List<CheckBox> multiCheckBox = new ArrayList<CheckBox>();
    private Label label;
    
    protected void initPreCheck() {
        // validate ListItems and EnumPairs
        if (parameter != null && ((CheckBoxListT) control).getListItem().size() != parameter.getEnumPair().size()) {
            throw new IllegalArgumentException("ListItems for Control \"" + control.getID() + "\" and EnumPairs for Parameter \"" + parameter.getName()
                    + "\" are not equal in number.");
        }
    }
    
    @Override
    protected void processReinit(Object aControlInitValue) {
        if (aControlInitValue != null) {
            setValue((String) aControlInitValue, true);
        } else {
            for (CheckBox tempButton : multiCheckBox) {
                if ((tempButton != null)) {
                    tempButton.setSelected(false);
                }
            }
        }
    }
    
    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
    }
    
    public String getParameterValue() {
        // Helper method from AbstractControlUI
        return getParameterValueAsMultipleValueString();
    }
    
    @Override
    public String getControlValueRaw() {
        String value = "";
        for (int i = 0; i < multiCheckBox.size(); i++) {
            CheckBox b = multiCheckBox.get(i);
            if (b.isSelected()) {
                if ("".equals(value)) {
                    value += ((CheckBoxListT) control).getListItem().get(i).getEnumID();
                } else {
                    value += " " + ((CheckBoxListT) control).getListItem().get(i).getEnumID();
                }
            }
        }
        return "".equals(value) ? null : value;
    }
    
    @Override
    public void setValue(String value) {
        this.setValue(value, false);
    }
    
    public void setValue(String value, boolean setValueAsControl) {
        List<String> values = Arrays.asList(value.split("\\s"));
        for (int i = 0; i < multiCheckBox.size(); i++) {
            CheckBox b = multiCheckBox.get(i);
            if (setValueAsControl || parameter == null) {
                String enumID = ((CheckBoxListT) control).getListItem().get(i).getEnumID();
                b.setSelected(values.contains(enumID));
            } else {
                String wireValue = parameter.getEnumPair().get(i).getWireValue();
                b.setSelected(values.contains(wireValue));
            }
        }
    }
    
    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        if (label != null) {
            widgets.add(label);
        }
        widgets.addAll(multiCheckBox);
        return widgets;
    }
    
    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.addAll(multiCheckBox);
        return widgets;
    }
    
    @Override
    public void addListener(JavaFXListener listener) {
        for (CheckBox b : multiCheckBox) {
            b.setOnAction(listener);
        }
    }
    
    @Override
    public void removeListener(JavaFXListener listener) {
        for (CheckBox b : multiCheckBox) {
            b.setOnAction(null);
        }
    }
    
    @Override
    public List< ? extends Node> createBrickComponents() {
        List<Node> components = new ArrayList<Node>();

        // tooltip
        String tooltip = control.getTooltip();

        // label
        if (control.getLabel() != null) {
            label = new Label();
            label.setText(control.getLabel());
            if (tooltip != null) {
                label.getTooltip().setText(tooltip);
            }
            components.add(label);
        }
        
        if (((CheckBoxListT) control).getOrientation() != null
                && PanelOrientationT.VERTICAL.equals(((CheckBoxListT) control)
                        .getOrientation())) {
            // TODO: NOT IMPLEMENTED
        } else {
            // TODO: NOT IMPLEMENTED
        }

        // checkBoxes
        List<ListItemT> listItems = ((CheckBoxListT) control).getListItem();
        for (ListItemT listItem : listItems) {
            
            CheckBox checkBox = new CheckBox(listItem.getUiRep());
            checkBox.setId(control.getParameterRef());
            //checkBox.setName(getName() + "/button/" + listItem.getEnumID());

            if (listItem.getUiRep() != null) {
                checkBox.setText(listItem.getUiRep());
            }
            
            if (parameter != null) {
                for (EnumPairT enumPair : parameter.getEnumPair()) {
                    if (enumPair.getEnumID() == null ? listItem.getEnumID() == null : enumPair.getEnumID().equals(listItem.getEnumID())) {
                        // set tooltip
                        if (enumPair.getDescription() != null) {
                            checkBox.getTooltip().setText(enumPair.getDescription());
                        } else if (tooltip != null) {
                            checkBox.getTooltip().setText(tooltip);
                        }
                        break;
                    }
                }
            } else {
                if (tooltip != null) {
                    checkBox.getTooltip().setText(tooltip);
                }
            }
            multiCheckBox.add(checkBox);
            components.add(checkBox);
        }

        // set initValue
        if (((CheckBoxListT) control).getInitValue() != null) {
            setValue(((CheckBoxListT) control).getInitValue(), true);
        }
        
        return components;
    }
}
