/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.atdl4j.ui.impl.AbstractAtdl4jWidget;
import org.atdl4j.ui.javafx.JavaFXWidget;

/**
 *
 * @author daniel.makgonta
 */
public abstract class AbstractJavaFXWidget<E extends Comparable<?>> extends AbstractAtdl4jWidget<E>
        implements JavaFXWidget<E> {

    protected List< ? extends Node> brickComponents;

    @Override
    public void setVisible(boolean visible) {
        for (Node control : getComponents()) {
            if (control != null) {
                control.setVisible(visible);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (Node control : getComponents()) {
            if (control != null) {
                control.setDisable(!enabled);
            }
        }
    }

    @Override
    public boolean isVisible() {
        for (Node control : getComponents()) {
            if ((control != null) && control.isVisible()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        for (Node control : getComponents()) {
            if ((control != null) && !control.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setControlExcludingLabelEnabled(boolean enabled) {
        for (Node control : getComponentsExcludingLabel()) {
            if (control != null) {
                control.setDisable(!enabled);
            }
        }
    }

    @Override
    public boolean isControlExcludingLabelEnabled() {
        for (Node control : getComponentsExcludingLabel()) {
            if ((control != null) && !control.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List< ? extends Node> getBrickComponents() {
        if (brickComponents == null) {
            brickComponents = createBrickComponents();
        }
        return brickComponents;
    }

    protected List< ? extends Node> createBrickComponents() {
        return new ArrayList<Node>();
    }

    @Override
    public void createWidget(Pane parent) {
        Pane wrapper = new VBox();
        List<? extends Node> components = getBrickComponents();

        for (Node node : components) {
            wrapper.getChildren().add(node);
        }
        parent.getChildren().add(wrapper);
    }
}
