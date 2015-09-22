package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.atdl4j.ui.impl.AbstractLabelWidget;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;
import org.atdl4j.ui.javafx.JavaFXWidget;

/**
 * @author daniel.makgonta
 */
public class JavaFXLabelWidget extends AbstractLabelWidget
        implements JavaFXWidget<String> {

    private List<Node> brickComponents;
    private Label label;

    @Override
    protected void processReinit(Object aControlInitValue) {
        if ((label != null)) {
            label.setText((aControlInitValue != null) ? (String) aControlInitValue : "");
        }
    }

    @Override
    public void setVisible(boolean visible) {
        for (Node control : getComponents()) {
            control.setVisible(visible);
        }
    }

    @Override
    public boolean isVisible() {
        for (Node control : getComponents()) {
            if (control.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (Node control : getComponents()) {
            control.setDisable(!enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        for (Node control : getComponents()) {
            if (!control.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(label);
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        return getComponents();
    }

    @Override
    public void addListener(JavaFXListener listener) {
        //do nothing
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        //do nothing
    }

    @Override
    public List<? extends Node> getBrickComponents() {
        if (brickComponents == null) {
            brickComponents = createBrickComponents();
        }
        return brickComponents;
    }

    private List<Node> createBrickComponents() {
        List<Node> components = new ArrayList<Node>();

        // label
        label = new Label();
        label.setPadding(new Insets(5, 5, 5, 5));

        if (control.getLabel() != null) {
            label.setText(control.getLabel());
        } else if (ControlHelper.getInitValue(control, getAtdl4jOptions()) != null) {
            label.setText((String) ControlHelper.getInitValue(control, getAtdl4jOptions()));
        } else {
            label.setText("");
        }

        // tooltip
        if (getTooltip() != null) {
            Tooltip tip = new Tooltip(getTooltip());
            label.setTooltip(tip);
        }

        components.add(label);
        return components;
    }

    @Override
    public void createWidget(Pane parent) {
        Pane wrapper = new AnchorPane();
        List<? extends Node> components = getBrickComponents();

        for (Node node : components) {
            wrapper.getChildren().add(node);
        }
        parent.getChildren().add(wrapper);
    }

}
