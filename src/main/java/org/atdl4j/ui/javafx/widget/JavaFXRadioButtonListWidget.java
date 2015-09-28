/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.RadioButtonListT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXRadioButtonListWidget extends AbstractJavaFXWidget<String> {

    private List<RadioButton> buttons = new ArrayList<RadioButton>();
    private ToggleGroup group = new ToggleGroup();
    private Label label;

    @Override
    protected void processReinit(Object aControlInitValue) {
        if (aControlInitValue != null) {
            // -- apply initValue if one has been specified --
            setValue((String) aControlInitValue, true);
        } else {
            // -- reset each when no initValue exists --
            for (RadioButton tempButton : buttons) {
                if ((tempButton != null)) {
                    tempButton.setSelected(false);
                }
            }
        }
    }

    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //do nothing
    }

    @Override
    public String getControlValueRaw() {
        for (int i = 0; i < this.buttons.size(); i++) {
            RadioButton b = buttons.get(i);
            if (b.isSelected()) {
                return ((RadioButtonListT) control).getListItem().get(i).getEnumID();
            }
        }
        return null;
    }

    @Override
    public void setValue(String value) {
        this.setValue(value, false);
    }

    public void setValue(String value, boolean setValueAsControl) {
        for (int i = 0; i < buttons.size(); i++) {
            RadioButton b = buttons.get(i);
            if (setValueAsControl || parameter == null) {
                b.setSelected(value.equals(getListItems().get(i).getEnumID()));
            } else {
                b.setSelected(value.equals(parameter.getEnumPair().get(i)
                        .getWireValue()));
            }
        }
    }

    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        if (label != null) {
            widgets.add(label);
        }
        widgets.addAll(buttons);
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.addAll(buttons);
        return widgets;
    }

    @Override
    public void addListener(JavaFXListener listener) {
        /*for (RadioButton b : buttons) {
         b.selectedProperty().addListener(listener);
         }*/
        group.selectedToggleProperty().addListener(listener);
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        /*for (RadioButton b : buttons) {
         b.selectedProperty().removeListener(listener);
         }*/
        group.selectedToggleProperty().removeListener(listener);
    }

    @Override
    public String getParameterValue() {
        return getParameterValueAsEnumWireValue();
    }

    @Override
    protected List< ? extends Node> createBrickComponents() {
        List<Node> components = new ArrayList<Node>();

        Pane wrapper = new HBox();
        String tooltip = getTooltip();

        // label
        if (control.getLabel() != null) {
            label = new Label();
            label.setText(control.getLabel());
            if (tooltip != null) {
                Tooltip tip = new Tooltip(tooltip);
                label.setTooltip(tip);
            }
            components.add(label);
        }

        /*
         //TODO: implement horiz/vert orientation for Swing
         if ( ((RadioButtonListT) control).getOrientation() != null &&
         PanelOrientationT.VERTICAL.equals( ((RadioButtonListT) control).getOrientation() ) )
         {
         c.setLayout( new GridLayout( 1, false ) );
         } else {
         RowLayout rl = new RowLayout();
         rl.wrap = false;
         c.setLayout( rl );
         }
         */
        // radioButton
        for (ListItemT listItem : ((RadioButtonListT) control).getListItem()) {
            RadioButton radioElement = new RadioButton();
            radioElement.setId(control.getParameterRef());
            radioElement.setPadding(new Insets(5, 10, 5, 5));
            radioElement.setText(listItem.getUiRep());
            Tooltip tip = new Tooltip();
            if (parameter != null) {
                for (EnumPairT enumPair : parameter.getEnumPair()) {
                    if (enumPair.getEnumID() == null ? listItem.getEnumID() == null : enumPair.getEnumID().equals(listItem.getEnumID())) {
                        tip.setText(enumPair.getDescription());
                        break;
                    }
                }
            } else {
                tip.setText(tooltip);
            }
            radioElement.setTooltip(tip);
            radioElement.setToggleGroup(group);
            buttons.add(radioElement);
            wrapper.getChildren().add(radioElement);
        }

        // set initValue (Note that this has to be the enumID, not the
        // wireValue)
        // set initValue
        if (ControlHelper.getInitValue(control, getAtdl4jOptions()) != null) {
            setValue((String) ControlHelper.getInitValue(control, getAtdl4jOptions()), true);
        }

        components.add(wrapper);
        return components;
    }
}
