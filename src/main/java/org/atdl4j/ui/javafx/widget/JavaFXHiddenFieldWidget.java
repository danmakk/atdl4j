package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.atdl4j.ui.impl.AbstractHiddenFieldWidget;
import org.atdl4j.ui.javafx.JavaFXListener;
import org.atdl4j.ui.javafx.JavaFXWidget;

/**
 * @author daniel.makgonta
 */
public class JavaFXHiddenFieldWidget extends AbstractHiddenFieldWidget
        implements JavaFXWidget<String> {
    
    private List<? extends Node> brickComponents;
    
    @Override
    protected void processReinit(Object aControlInitValue) {
        // do nothing
    }
    
    @Override
    public List<Node> getComponents() {
        return null;
    }
    
    @Override
    public List<Node> getComponentsExcludingLabel() {
        return getComponents();
    }
    
    @Override
    public void addListener(JavaFXListener listener) {
        // do nothing
    }
    
    @Override
    public void removeListener(JavaFXListener listener) {
        // do nothing
    }
    
    @Override
    public List<? extends Node> getBrickComponents() {
        if (brickComponents == null) {
            brickComponents = createBrickComponents();
        }
        return brickComponents;
    }
    
    public List< ? extends Node> createBrickComponents() {
        return new ArrayList<Node>();
    }
    
    @Override
    public void createWidget(Pane parent) {
        Pane wrapper = new AnchorPane();
        wrapper.setPadding(new Insets(5, 5, 5, 5));
        parent.getChildren().add(wrapper);
        List<? extends Node> components = getBrickComponents();
        
        for (Node node : components) {
            wrapper.getChildren().add(node);
        }
        parent.getChildren().add(wrapper);
    }
}
