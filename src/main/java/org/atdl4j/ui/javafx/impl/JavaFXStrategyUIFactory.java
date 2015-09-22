/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atdl4j.ui.javafx.impl;

import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.atdl4j.data.Atdl4jHelper;
import org.atdl4j.fixatdl.core.StrategyT;
import org.atdl4j.ui.StrategyUI;
import static org.atdl4j.ui.impl.BaseStrategyUIFactory.createStrategyUI;
import static org.atdl4j.ui.impl.BaseStrategyUIFactory.getAtdl4jUserMessageHandler;

/**
 * @author daniel.makgonta
 */
public class JavaFXStrategyUIFactory {

    protected static final Logger logger = Logger.getLogger(JavaFXStrategyUIFactory.class);

    public static StrategyUI createStrategyUIAndContainer(JavaFXStrategiesUI aStrategiesUI, StrategyT aStrategy) {
        Pane strategyParent = aStrategiesUI.getStrategiesPanel();
        StrategyUI ui;

        try {
            ui = createStrategyUI(aStrategy, aStrategiesUI.getStrategies(), aStrategiesUI.getStrategiesRules(), strategyParent, aStrategiesUI.getAtdl4jOptions());
        } catch (Throwable e) {
            getAtdl4jUserMessageHandler().displayException("Strategy Load Error",
                    "Error in Strategy: " + Atdl4jHelper.getStrategyUiRepOrName(aStrategy), e);

            // rollback changes
            strategyParent.getChildren().removeAll();

            // skip to next strategy
            return null;
        }

        ui.setCxlReplaceMode(aStrategiesUI.getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode());
        return ui;
    }
}
