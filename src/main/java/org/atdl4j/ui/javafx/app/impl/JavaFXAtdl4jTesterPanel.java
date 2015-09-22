package org.atdl4j.ui.javafx.app.impl;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.data.exception.ValidationException;
import org.atdl4j.ui.Atdl4jWidget;
import org.atdl4j.ui.AtdlWidgetListener;
import org.atdl4j.ui.app.Atdl4jCompositePanel;
import org.atdl4j.ui.app.StrategySelectionEvent;
import org.atdl4j.ui.app.impl.AbstractAtdl4jTesterPanel;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXAtdl4jTesterPanel extends AbstractAtdl4jTesterPanel {

    public final Logger logger = Logger.getLogger(JavaFXAtdl4jTesterPanel.class);
    private Pane parentComposite;

    private BorderPane inputAndFilterDataAndLoadMessageComposite;
    private BorderPane validateOutputSection;

    private CheckMenuItem showFileSelectionMenuItem;
    private CheckMenuItem showValidateOutputMenuItem;

    private Pane okCancelButtonSection;
    private TextField outputFixMessageText;

    @Override
    protected Object createValidateOutputSection() {
        validateOutputSection = new BorderPane();
        validateOutputSection.setTop(new Label("Validation"));
        // validate button
        Button validateButton = new Button("Validate Output");
        validateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    validateButtonSelected();
                } catch (ValidationException ex) {
                    logger.info("Validation Exception:", ex);
                    getAtdl4jUserMessageHandler().displayException(
                            "Validation Exception", "", ex);
                }
            }
        });
        validateOutputSection.setLeft(validateButton);
        outputFixMessageText = new TextField();
        validateOutputSection.setCenter(outputFixMessageText);
        return validateOutputSection;
    }

    public Pane buildAtdl4jTesterPanel(Pane frame, Atdl4jOptions aAtdl4jOptions) {

        BorderPane mainPanel = new BorderPane();
        frame.getChildren().add(mainPanel);

        parentComposite = frame;

        VBox box = new VBox();
        parentComposite.getChildren().add(box);

        // -- Delegate back to AbstractAtdl4jTesterPanel -- 
        init(parentComposite, aAtdl4jOptions);
        inputAndFilterDataAndLoadMessageComposite = new BorderPane();
        inputAndFilterDataAndLoadMessageComposite.setTop(new Label("Testing Input"));
        // -- Build the Swing.JPanel from Atdl4jInputAndFilterDataSelectionPanel ("Input Data/Filter Criteria" button) --
        Atdl4jCompositePanel atdl4jCompositePanel = getAtdl4jCompositePanel();

        BorderPane inpPanel = (BorderPane) getAtdl4jInputAndFilterDataSelectionPanel().buildAtdl4jInputAndFilterDataSelectionPanel(inputAndFilterDataAndLoadMessageComposite, getAtdl4jOptions(), atdl4jCompositePanel.getAtdl4jUserMessageHandler());
        if (inpPanel != null) {
            inputAndFilterDataAndLoadMessageComposite.setLeft(inpPanel);
        }
        // -- Build the Swing.JPanel from FixMsgLoadPanel ("Load FIX Message" button) --
        BorderPane loadPanel = (BorderPane) getFixMsgLoadPanel().buildFixMsgLoadPanel(inputAndFilterDataAndLoadMessageComposite, getAtdl4jOptions());
        inputAndFilterDataAndLoadMessageComposite.setCenter(loadPanel);

        box.getChildren().add(inputAndFilterDataAndLoadMessageComposite);
        box.getChildren().add(getEnableDisablePanel(atdl4jCompositePanel));

        BorderPane internalPanel = new BorderPane();
        internalPanel.setCenter(new Pane());
        Pane center = (Pane) internalPanel.getCenter();
        Pane bottom = (Pane) internalPanel.getBottom();

        // -- Build the JavaFX.Pane from FixatdlFileSelectionPanel (filename / file dialog) --
        Pane loadFilePanel = (Pane) getFixatdlFileSelectionPanel().buildFixatdlFileSelectionPanel(parentComposite, getAtdl4jOptions());
        center.getChildren().add(loadFilePanel);

        // -- Build the JavaFX.Pane from Atdl4jCompositePanel --
        Pane strategyParamsPanel = (Pane) atdl4jCompositePanel.buildAtdl4jCompositePanel(parentComposite, aAtdl4jOptions);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(strategyParamsPanel);
        center.getChildren().add(scrollPane);

        // -- Build the JavaFX.Pane containing "Validate Output" button and outputFixMessageText --
        Pane validatePanel = (Pane) createValidateOutputSection();

        if (bottom == null) {
            bottom = new AnchorPane();
        }

        bottom.getChildren().add(validatePanel);

        box.getChildren().add(internalPanel);

        // -- Build the Swing.JPanel containing "OK" and "Cancel" buttons --
        Pane buttonsPanel = createOkCancelButtonSection();
        parentComposite.getChildren().add(buttonsPanel);

        // -- Build the Swing JMenuItems --
        createPopupMenuForPanel(internalPanel);

        addListeners(atdl4jCompositePanel);

        return parentComposite;
    }

    protected Pane createOkCancelButtonSection() {
        okCancelButtonSection = new HBox();

        // OK button
        Button okButton = new Button("OK");
        okButton.setTooltip(new Tooltip("Validate and accept the specified strategy and parameters"));
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                okButtonSelected();
            }
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setTooltip(new Tooltip("Cancel ignoring any specified changes"));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cancelButtonSelected();
            }
        });

        okCancelButtonSection.getChildren().add(okButton);
        okCancelButtonSection.getChildren().add(cancelButton);

        setVisibleOkCancelButtonSection(Atdl4jConfig.getConfig().isShowTesterPanelOkCancelButtonSection());

        return okCancelButtonSection;
    }

    private Node getEnableDisablePanel(final Atdl4jCompositePanel atdl4jCompositePanel) {
        AnchorPane panel = new AnchorPane();

        CheckBox enableCheckbox = new CheckBox("Enable/disable");
        enableCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldSelected, Boolean newSelected) {
                atdl4jCompositePanel.setEditable(!newSelected);
            }
        });

        panel.getChildren().add(enableCheckbox);

        return panel;
    }

    private void addListeners(Atdl4jCompositePanel atdl4jCompositePanel) {
        atdl4jCompositePanel.getStrategiesUI().addWidgetListener(new AtdlWidgetListener() {
            @Override
            public void widgetChanged(Atdl4jWidget widget) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Widget changed notification :" + widget);
                }
            }
        });
    }

    @Override
    public void closePanel() {
        if (parentComposite != null) {
            Pane parent = (Pane)parentComposite.getParent();
            if(parent != null)
            parent.getChildren().remove(parentComposite);
        }
    }

    @Override
    public void setVisibleFileSelectionSection(boolean aVisible) {
        if (getFixatdlFileSelectionPanel() != null) {
            getFixatdlFileSelectionPanel().setVisible(aVisible);
            if (showFileSelectionMenuItem != null) {
                showFileSelectionMenuItem.setDisable(!aVisible);
            }
        }
    }

    protected void createPopupMenuForPanel(Pane _panel) {
        final ContextMenu menu = new ContextMenu();

        // -- "Show File Selection" --
        setVisibleFileSelectionSection(Atdl4jConfig.getConfig().isShowFileSelectionSection());
        showFileSelectionMenuItem = new CheckMenuItem("Show File Selection");
        showFileSelectionMenuItem.setSelected(Atdl4jConfig.getConfig().isShowFileSelectionSection());
        showFileSelectionMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setVisibleFileSelectionSection(showFileSelectionMenuItem.isSelected());
            }
        });

        // -- "Show Validate Output" --
        setVisibleValidateOutputSection(Atdl4jConfig.getConfig().isShowValidateOutputSection());
        showValidateOutputMenuItem = new CheckMenuItem("Show Validate Output");
        showValidateOutputMenuItem.setSelected(Atdl4jConfig.getConfig().isShowValidateOutputSection());
        showValidateOutputMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setVisibleValidateOutputSection(showValidateOutputMenuItem.isSelected());
            }
        });

        menu.getItems().addAll(showFileSelectionMenuItem, showValidateOutputMenuItem);

        _panel.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    menu.show(new Pane(), event.getScreenX(), event.getScreenY());
                } else {

                }
            }
        });

    }

    @Override
    public void setVisibleValidateOutputSection(boolean aVisible) {
        if (validateOutputSection != null) {
            validateOutputSection.setVisible(aVisible);
            if (showValidateOutputMenuItem != null) {
                showValidateOutputMenuItem.setSelected(aVisible);
            }
        }
    }

    @Override
    public void setVisibleTestingInputSection(boolean aVisible) {
        //do nothing
    }

    @Override
    public Object buildAtdl4jTesterPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) {
        return buildAtdl4jTesterPanel((Pane) aParentOrShell, aAtdl4jOptions);
    }

    @Override
    public void setVisibleOkCancelButtonSection(boolean aVisible) {
        if (okCancelButtonSection != null) {
            okCancelButtonSection.setVisible(aVisible);
        }
    }

    @Override
    public void beforeStrategyIsSelected(StrategySelectionEvent event) {
        //do nothing
    }

    private Pane getShell() {
        if (getParentComposite() != null) {
            return (Pane) getParentComposite().getParent();
        } else {
            return null;
        }
    }

    private Pane getParentComposite() {
        return this.parentComposite;
    }

    @Override
    protected void setValidateOutputText(String aText) {
        if (outputFixMessageText != null) {
            if ((Atdl4jConfig.getConfig().isShowValidateOutputSection())) {
                if (aText != null) {
                    outputFixMessageText.setText(aText.replace('\n', ' '));
                } else {
                    outputFixMessageText.setText("");
                }
            } else {
                outputFixMessageText.setText(aText.replace('\n', ' '));
            }
        }
    }
}
