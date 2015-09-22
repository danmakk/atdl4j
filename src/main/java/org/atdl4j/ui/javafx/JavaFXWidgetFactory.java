package org.atdl4j.ui.javafx;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;

import javafx.scene.Parent;

public interface JavaFXWidgetFactory {
	public JavaFXWidget<?> createWidget(Parent parent, ControlT control, ParameterT parameter);
}
