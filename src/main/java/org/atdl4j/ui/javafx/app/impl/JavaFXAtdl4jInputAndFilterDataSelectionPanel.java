package org.atdl4j.ui.javafx.app.impl;

import org.atdl4j.config.Atdl4jConfig;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.Atdl4jUserMessageHandler;
import org.atdl4j.ui.app.impl.AbstractAtdl4jInputAndFilterDataSelectionPanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

public class JavaFXAtdl4jInputAndFilterDataSelectionPanel extends AbstractAtdl4jInputAndFilterDataSelectionPanel {
	private Parent parentPanel;
	private Button atdl4jInputAndFilterDataPanelButton;
	private Dialog<String> atdl4jInputAndFilterDataPanelDialog;

	private CheckBox debugModeButton;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataSelectionPanel#
	 * buildAtdl4jInputAndFilterDataSelectionPanel(java.lang.Object,
	 * org.atdl4j.config.Atdl4jOptions,
	 * org.atdl4j.ui.app.Atdl4jUserMessageHandler)
	 */
	@Override
	public Object buildAtdl4jInputAndFilterDataSelectionPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions,
			Atdl4jUserMessageHandler aAtdl4jUserMessageHandler) {
		return buildAtdl4jInputAndFilterDataSelectionPanel((Parent) aParentOrShell, aAtdl4jOptions,
				aAtdl4jUserMessageHandler);
	}

	public Pane buildAtdl4jInputAndFilterDataSelectionPanel(Parent aParentOrShell, Atdl4jOptions aAtdl4jOptions,
			Atdl4jUserMessageHandler aAtdl4jUserMessageHandler) {
		parentPanel = (Parent) aParentOrShell;

		// -- Delegate back to AbstractAtdl4jInputAndFilterDataSelectionPanel --
		init(aParentOrShell, aAtdl4jOptions, aAtdl4jUserMessageHandler);

		Pane panel = new BorderPane();

		atdl4jInputAndFilterDataPanelButton = new Button("Input And Filter Data");
		atdl4jInputAndFilterDataPanelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				buttonInputAndFilterDataSelected();
			}
		});

		BorderPane tmp = (BorderPane) panel;
		tmp.setCenter(atdl4jInputAndFilterDataPanelButton);

		return panel;
	}

	private void closeAtdl4jInputAndFilterDataPanelDialog() {
		if (atdl4jInputAndFilterDataPanelDialog != null) {
			atdl4jInputAndFilterDataPanelDialog.getDialogPane().setVisible(false);
		}
	}

	private void buttonInputAndFilterDataSelected() {
		if (atdl4jInputAndFilterDataPanelDialog == null) {
			atdl4jInputAndFilterDataPanelDialog = createAtdl4jInputAndFilterDataPanelDialog();
		}

		getAtdl4jInputAndFilterDataPanel().loadScreenWithAtdl4jOptions();
		getDebugModeButton().setSelected(Atdl4jConfig.getConfig().isDebugLoggingLevel());

		atdl4jInputAndFilterDataPanelDialog.getDialogPane().setVisible(true);
	}

	private void buttonOkSelected() {
		// -- Atdl4jInputAndFilterDataPanel.extractAtdl4jOptionsFromScreen()
		// populates/overlays data members within our Atdl4jOptions --
		if (!getAtdl4jInputAndFilterDataPanel().extractAtdl4jOptionsFromScreen()) {
			getAtdl4jUserMessageHandler().displayMessage("Error",
					"Error extracting Atdl4jOptions extracted from screen");
			return;
		}
		fireInputAndFilterDataSpecifiedEvent(getAtdl4jOptions().getInputAndFilterData());
		closeAtdl4jInputAndFilterDataPanelDialog();
	}

	private void buttonCancelSelected() {
		closeAtdl4jInputAndFilterDataPanelDialog();
	}

	private Dialog createAtdl4jInputAndFilterDataPanelDialog() {

		Dialog<String> tempDialog = new Dialog<String>();
		tempDialog.setTitle("atdl4j Input and Filter Data / Configuration Settings");
		tempDialog.initModality(Modality.WINDOW_MODAL);

		getAtdl4jInputAndFilterDataPanel().buildAtdl4jInputAndFilterDataPanel(tempDialog, getAtdl4jOptions());

		AnchorPane footerPanel = new AnchorPane();
		BorderPane tmp = new BorderPane();
		tmp.setBottom(footerPanel);

		tempDialog.getDialogPane().getChildren().add(tmp);

		Button okButton = new Button("OK");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				buttonOkSelected();
			}
		});
		footerPanel.getChildren().add(okButton);

		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				buttonCancelSelected();
			}
		});
		footerPanel.getChildren().add(cancelButton);

		setDebugModeButton(new CheckBox("Debug Mode"));
		getDebugModeButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Atdl4jConfig.getConfig().setDebugLoggingLevel(getDebugModeButton().isSelected());
			}
		});
		footerPanel.getChildren().add(getDebugModeButton());

		tempDialog.getDialogPane().autosize();
		return tempDialog;
	}

	public CheckBox getDebugModeButton() {
		return debugModeButton;
	}

	public void setDebugModeButton(CheckBox debugModeButton) {
		this.debugModeButton = debugModeButton;
	}
}