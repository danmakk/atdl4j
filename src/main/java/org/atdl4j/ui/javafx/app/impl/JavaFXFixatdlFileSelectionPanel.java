package org.atdl4j.ui.javafx.app.impl;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractFixatdlFileSelectionPanel;

public class JavaFXFixatdlFileSelectionPanel extends AbstractFixatdlFileSelectionPanel {

    Parent parentComposite;
    Pane composite;

    TextArea filepathText;
    Button browseButton;

    @Override
    public Object buildFixatdlFileSelectionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions) {
        return buildFixatdlFileSelectionPanel((Parent) parentOrShell, atdl4jOptions);
    }

    public Pane buildFixatdlFileSelectionPanel(Parent aParentComposite, Atdl4jOptions atdl4jOptions) {
        setAtdl4jOptions(atdl4jOptions);
        setParentComposite(aParentComposite);

        BorderPane tmp = new BorderPane();

        tmp.setTop(new Label("Load FIXatdl XML File"));

        filepathText = new TextArea();

        // -- Handle Enter key within Text field --
        filepathText.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if (!"".equals(filepathText.getText().trim())) {
                        fireFixatdlFileSelectedEvent(filepathText.getText());
                    }
                }
            }
        });

        browseButton = new Button("...");
        browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();
                fc.setTitle("Browse FIXatdl XML Files");
                fc.setInitialDirectory(new File(System.getProperty("user.home")));
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));

                File file = fc.showOpenDialog(new Stage());

                if (file != null && file.isFile()) {
                    filepathText.setText(file.getPath());
                    fireFixatdlFileSelectedEvent(file.getPath());
                }
            }
        });

        tmp.setCenter(filepathText);
        tmp.setRight(browseButton);

        composite = tmp;

        return composite;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.atdl4j.ui.app.FixatdlFileSelectionPanel#selectFilename(java.lang.
     * String)
     */
    @Override
    public void selectFilename(String aFilename) {
        if (filepathText != null) {
            filepathText.setText(aFilename);
            fireFixatdlFileSelectedEvent(aFilename);
        }
    }

    /**
     * Returns getParentComposite().getShell().
     *
     * @return the shell
     */
    private Scene getShell() {
        if (getParentComposite() != null) {
            return getParentComposite().getScene();
        } else {
            return null;
        }
    }

    /**
     * @return the parentComposite
     */
    Parent getParentComposite() {
        return this.parentComposite;
    }

    /**
     * @param aParentComposite the parentComposite to set
     */
    private void setParentComposite(Parent aParentComposite) {
        this.parentComposite = aParentComposite;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.FixatdlFileSelectionPanel#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean aVisible) {
        if (composite != null) {
            composite.setVisible(aVisible);
        }
    }
}
