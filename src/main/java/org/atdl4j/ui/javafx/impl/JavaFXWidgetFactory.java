/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.impl;

import javafx.scene.layout.Pane;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.javafx.JavaFXWidget;
import static org.atdl4j.ui.javafx.impl.JavaFXStrategyPanelFactory.logger;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXWidgetFactory {

    // Used to create a single parameter widget

    public JavaFXWidget<?> createWidget(Pane parent, ControlT control, ParameterT parameter, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory) {
        JavaFXWidget<?> parameterWidget = null;

        logger.debug("createWidget() invoked " + "with parms parent: " + parent
                + " control: " + control + " parameter: " + parameter + " style: " + style);

        parameterWidget = (JavaFXWidget<?>) aAtdl4jWidgetFactory.create(control, parameter);

        logger.debug("createWidget() returned parameterWidget: " + parameterWidget);

        parameterWidget.createWidget(parent);
        logger.debug("createWidget() completed.  parameterWidget: " + parameterWidget);

        parameterWidget.applyConstOrInitValues();

        return parameterWidget;
    }
}
