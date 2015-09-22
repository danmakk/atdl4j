package org.atdl4j.ui.javafx.impl;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.fixatdl.layout.BorderT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.StrategyPanelHelper;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXStrategyPanelHelper implements StrategyPanelHelper {

    public static Pane createStrategyPanelContainer(StrategyPanelT aStrategyPanel, Pane aParent, int aStyle) {
        VBox c = new VBox();
        c.setPadding(new Insets(8, 8, 8, 8));

        if (aStrategyPanel.getTitle() != null) {
            HBox box = new HBox();
            Label title = new Label(aStrategyPanel.getTitle());
            box.getChildren().add(title);
            c.getChildren().add(box);
        } // -- Check for Line border (no title) --
        else if (BorderT.LINE.equals(aStrategyPanel.getBorder())) {
            HBox box = new HBox();
            Label title = new Label(aStrategyPanel.getTitle());
            box.getChildren().add(title);
            c.getChildren().add(box);
        }
        
        return c;
    }

    public static void createStrategyPanelSpringLayout(StrategyPanelT _panelT, Pane _panel) throws FIXatdlFormatException {
        Pane box;

        PanelOrientationT orientation = _panelT.getOrientation();
        if (orientation == PanelOrientationT.HORIZONTAL) {
            box = new HBox();
        } else if (orientation == PanelOrientationT.VERTICAL) {
            box = new VBox();
        } else {
            throw new FIXatdlFormatException("StrategyPanel (" + _panelT.getTitle() + ") is missing orientation attribute.");
        }
        
        if (_panel.getChildren().size() > 0) {
            box.getChildren().addAll(_panel.getChildren());
        }
        
        box.setPadding(new Insets(8, 8, 8, 8));
        _panel = box;
    }

    /**
     * Navigates through aWidget's getParent() looking for ExpandBar with
     * ExpandItems not-yet-expanded and expands those
     *
     * @param aWidget
     * @return boolean indicating whether any ExpandBar ExpandItems were
     * adjusted
     */
    public boolean expandAtdl4jWidgetParentStrategyPanel(Atdl4jWidget<?> aWidget) {

        return true;
    }
}
