package org.atdl4j.ui.javafx;

import java.util.Map;
import javafx.scene.Parent;

import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;

public interface JavaFXPanelFactory {
	public Map<String, JavaFXWidget<?>> createStrategyPanelAndWidgets(Parent parent, StrategyPanelT panel,
			Map<String, ParameterT> parameters);
}
