package org.atdl4j.ui.javafx.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.atdl4j.data.exception.FIXatdlFormatException;
import org.atdl4j.fixatdl.core.ParameterT;
import org.atdl4j.fixatdl.layout.ControlT;
import org.atdl4j.fixatdl.layout.PanelOrientationT;
import org.atdl4j.fixatdl.layout.StrategyPanelT;
import org.atdl4j.ui.Atdl4jWidgetFactory;
import org.atdl4j.ui.javafx.JavaFXWidget;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXStrategyPanelFactory {

    protected static final Logger logger = Logger.getLogger(JavaFXStrategyPanelFactory.class);
    private static JavaFXWidgetFactory javafxWidgetFactory = new JavaFXWidgetFactory();

    // Given a panel, recursively populates a map of Panels and Parameter widgets
    // Can also process options for a group frame instead of a single panel
    public Map<String, JavaFXWidget<?>> createStrategyPanelAndWidgets(Pane parent, StrategyPanelT panel,
            Map<String, ParameterT> parameters, int style, Atdl4jWidgetFactory aAtdl4jWidgetFactory) throws FIXatdlFormatException {
        logger.debug("createStrategyPanelAndWidgets(Composite parent, StrategyPanelT panel, Map<String, ParameterT> parameters, int style)" + " invoked with parms parent: "
                + parent + " panel: " + panel + " parameters: " + parameters + " style: " + style);
        Map<String, JavaFXWidget<?>> controlWidgets = new HashMap<String, JavaFXWidget<?>>();

        Pane c = JavaFXStrategyPanelHelper.createStrategyPanelContainer(panel, parent, style);
        parent.getChildren().add(c);

        if (panel.getStrategyPanel().size() > 0 && panel.getControl().size() > 0) {
            // -- Wrap each Control with an auto-built StrategyPanel if setting is true --
            if (aAtdl4jWidgetFactory.getAtdl4jOptions().isAccommodateMixOfStrategyPanelsAndControls()) {
                // -- FIXatdl 1.1 spec recommends against vs. prohibits.  Mixed list may not be displayed 'in sequence' of file. --
                logger.warn("StrategyPanel contains both StrategyPanel (" + panel.getStrategyPanel().size() + ") and Control ( " + panel.getControl().size() + " elements.\nSee Atdl4jOptions.setAccommodateMixOfStrategyPanelsAndControls() as potential work-around, though Controls will appear after StrategyPanels.");

                StrategyPanelT tempPanel = new StrategyPanelT();
                tempPanel.setCollapsible(Boolean.FALSE);
                tempPanel.setCollapsed(Boolean.FALSE);
                tempPanel.setOrientation(panel.getOrientation());
                tempPanel.setColor(panel.getColor());

                logger.warn("Creating a StrategyPanel to contain " + panel.getControl().size() + " Controls.");
                tempPanel.getControl().addAll(panel.getControl());
                panel.getControl().clear();
                panel.getStrategyPanel().add(tempPanel);
            } else {
                // 7/20/2010 -- original behavior:
                throw new FIXatdlFormatException("StrategyPanel may not contain both StrategyPanel and Control elements.");
            }
        }

        // build panels widgets recursively
        for (StrategyPanelT p : panel.getStrategyPanel()) {
            Map<String, JavaFXWidget<?>> widgets = createStrategyPanelAndWidgets(c, p, parameters, style, aAtdl4jWidgetFactory);
            // check for duplicate IDs
            for (String newID : widgets.keySet()) {
                for (String existingID : controlWidgets.keySet()) {
                    if (newID.equals(existingID)) {
                        throw new FIXatdlFormatException("Duplicate Control ID: \"" + newID + "\"");
                    }
                }
            }
            controlWidgets.putAll(widgets);
        }

        // build control widgets recursively 
        for (ControlT control : panel.getControl()) {
            ParameterT parameter = null;

            if (control.getParameterRef() != null) {
                parameter = parameters.get(control.getParameterRef());
                if (parameter == null) {
                    throw new FIXatdlFormatException("Cannot find Parameter \"" + control.getParameterRef() + "\" for Control ID: \"" + control.getID() + "\"");
                }
            }
            JavaFXWidget<?> widget = javafxWidgetFactory.createWidget((Pane) c, control, parameter, style, aAtdl4jWidgetFactory);

            widget.setParentStrategyPanel(panel);
            widget.setParent(c);

            // check for duplicate Control IDs
            if (control.getID() != null) {
                // check for duplicate Control IDs
                for (JavaFXWidget<?> w : controlWidgets.values()) {
                    if (w.getControl().getID().equals(control.getID())) {
                        throw new FIXatdlFormatException("Duplicate Control ID: \"" + control.getID() + "\"");
                    }
                }
                controlWidgets.put(control.getID(), widget);
            } else {
                throw new FIXatdlFormatException("Control Type: \"" + control.getClass().getSimpleName() + "\" is missing ID");
            }
        }

        return controlWidgets;
    }

    /**
     * Display panel's children inside the given parent
     *
     * @throws FIXatdlFormatException
     */
    public Pane layoutStrategyPanel(Pane parent, StrategyPanelT panel, int style, Map<String, JavaFXWidget< ?>> widgets, int depth)
            throws FIXatdlFormatException {
        int rowIndex = 0;

        // Call recursively on sub-panels
        for (StrategyPanelT sp : panel.getStrategyPanel()) {
            // create a container
            Pane c = JavaFXStrategyPanelHelper.createStrategyPanelContainer(sp, parent, style);

            PanelOrientationT orientation = sp.getOrientation();
            String orient = orientation.value();

            Pane container;

            if ("VERTICAL".equals(orient.trim())) {
                container = new VBox();
            } else { //HORIZONTAL
                container = new HBox();
            }

            c.getChildren().add(container);
            // recursive call
            Pane childPanel = layoutStrategyPanel(c,
                    sp,
                    style,
                    widgets,
                    depth + 1);
            parent.getChildren().add(childPanel);
            rowIndex++;
        }

        for (ControlT c : panel.getControl()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Control :" + c.getLabel() + " rowIndex=" + rowIndex);
            }

            JavaFXWidget<?> javaFXWidget = widgets.get(c.getID());

            List< ? extends Node> brickComponents = javaFXWidget
                    .getBrickComponents();

            if ((brickComponents != null) && !brickComponents.isEmpty()) {

                for (Node comp : brickComponents) {
                    if (logger.isDebugEnabled()) {
                        if (comp instanceof Parent) {
                            logger.debug("Label ");
                        }
                    }
                    parent.getChildren().add(comp);
                }
                rowIndex++;
            }
        }
        
        parent.getChildren().add(new Pane());
        return (Pane) parent;
    }

    public Pane createStrategyPanel(Pane parent, StrategyPanelT panel,
            int style,
            Map<String, JavaFXWidget< ?>> widgets,
            int depth)
            throws FIXatdlFormatException {
        return layoutStrategyPanel(parent, panel, style, widgets, depth);
    }
}
