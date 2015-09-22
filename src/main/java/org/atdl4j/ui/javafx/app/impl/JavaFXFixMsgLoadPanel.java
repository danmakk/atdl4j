package org.atdl4j.ui.javafx.app.impl;

import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractFixMsgLoadPanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class JavaFXFixMsgLoadPanel extends AbstractFixMsgLoadPanel {

    Parent parentContainer;

    // JPanel as we need TitledBorder private Container container;
    TextArea fixMsgText;
    Button loadFixMsgButton;

    @Override
    public Object buildFixMsgLoadPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions) {
        return buildFixMsgLoadPanel((Parent) parentOrShell, atdl4jOptions);
    }

    public Pane buildFixMsgLoadPanel(Parent aParentContainer, Atdl4jOptions atdl4jOptions) {
        setAtdl4jOptions(atdl4jOptions);
        setParentContainer(aParentContainer);

        BorderPane tempContainer = new BorderPane();
        tempContainer.setTop(new Label("Pre-populate with FIX Message Fragment (tag=value syntax)"));

        loadFixMsgButton = new Button("Load Message");
        tempContainer.setLeft(loadFixMsgButton);

        loadFixMsgButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadFixMessage();
            }
        });

        fixMsgText = new TextArea();
        tempContainer.setCenter(fixMsgText);
        // -- Handle Enter key within Text field --
        fixMsgText.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    loadFixMessage();
                }
            }
        });

        return tempContainer;
    }

    private void loadFixMessage() {
        fireFixMsgLoadSelectedEvent(fixMsgText.getText());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.FixMsgLoadPanel#selectFilename(java.lang.String)
     */
    @Override
    public void setFixMsg(String aFixMsg) {
        if (fixMsgText != null) {
            fixMsgText.setText(aFixMsg);
            fireFixMsgLoadSelectedEvent(aFixMsg);
        }
    }

    /**
     * @return the parentContainer
     */
    private Parent getParentContainer() {
        return this.parentContainer;
    }

    /**
     * @param aParentContainer the parentContainer to set
     */
    private void setParentContainer(Parent aParentContainer) {
        this.parentContainer = aParentContainer;
    }

}
