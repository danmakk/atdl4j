package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 * @author daniel.makgonta
 */
public class JavaFXTextFieldWidget extends AbstractJavaFXWidget<String> {

    private TextField textField;
    private Label label;
    private Pane wrapper;

    @Override
    public void createWidget(Pane parent) {
        // tooltip
        String tooltip = control.getTooltip();

        // label		
        if (control.getLabel() != null) {
            label = new Label();
            label.setPadding(new Insets(5, 5, 5, 5));
            label.setText(control.getLabel());
            if (tooltip != null) {
                Tooltip tip = new Tooltip(tooltip);
                label.setTooltip(tip);
            }
        }

        // textField
        textField = new TextField();
        textField.setMinWidth(10);
        textField.setPadding(new Insets(0, 5, 5, 5));

        // init value
        if (ControlHelper.getInitValue(control, getAtdl4jOptions()) != null) {
            textField.setText((String) ControlHelper.getInitValue(control, getAtdl4jOptions()));
        }

        // tooltip
        if (tooltip != null) {
            textField.setTooltip(new Tooltip(tooltip));
        }

        if (label != null) {
            wrapper = new VBox();
            wrapper.getChildren().add(label);
            wrapper.getChildren().add(textField);
            parent.getChildren().add(wrapper);
        } else {
            parent.getChildren().add(textField);
        }

        textField.setPrefSize(100, textField.getPrefHeight());
    }

    @Override
    public void setVisible(boolean visible) {
        if (wrapper != null) {
            wrapper.setVisible(visible);
        } else {
            super.setVisible(visible);
        }
    }

    @Override
    protected void processReinit(Object aControlInitValue) {
        if ((textField != null)) {
            textField.setText((aControlInitValue != null) ? (String) aControlInitValue : "");
        }
    }

    @Override
    public String getControlValueRaw() {
        String value = textField.getText();

        if ("".equals(value)) {
            return null;
        } else {
            return value;
        }
    }

    @Override
    public void setValue(String value) {
        textField.setText((value == null) ? "" : value.toString());
    }

    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        if (label != null) {
            widgets.add(label);
        }
        widgets.add(textField);
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(textField);
        return widgets;
    }

    @Override
    public void addListener(JavaFXListener listener) {
        textField.textProperty().addListener(listener);
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        textField.textProperty().removeListener(listener);
    }

    @Override
    public void processConstValueHasBeenSet() {
        textField.setEditable(false);
    }

    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        if (Boolean.FALSE.equals(aNewNullValueInd)) {
            textField.setText("");
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
                label.setTooltip(new Tooltip(tooltip));
            }
        }

        // textField
        textField = new TextField();

        // init value
        if (ControlHelper.getInitValue(control, getAtdl4jOptions()) != null) {
            textField.setText((String) ControlHelper.getInitValue(control, getAtdl4jOptions()));
        }

        // tooltip
        if (tooltip != null) {
            textField.setTooltip(new Tooltip(tooltip));
        }

        if (label != null) {
            components.add(label);
        }
        
        components.add(textField);
        textField.setPrefSize(100, textField.getPrefHeight());
        return components;
    }
}
