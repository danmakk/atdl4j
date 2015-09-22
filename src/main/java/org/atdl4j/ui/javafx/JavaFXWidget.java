package org.atdl4j.ui.javafx;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.atdl4j.ui.Atdl4jWidget;

public interface JavaFXWidget<E extends Comparable<?>> extends Atdl4jWidget<E> {

    public void createWidget(Pane parent);

    public List<Node> getComponents();

    public List<Node> getComponentsExcludingLabel();

    public void addListener(JavaFXListener listener);

    public void removeListener(JavaFXListener listener);

    public List< ? extends Node> getBrickComponents();
}
