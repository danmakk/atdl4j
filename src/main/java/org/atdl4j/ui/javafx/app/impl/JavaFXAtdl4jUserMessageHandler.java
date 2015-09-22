package org.atdl4j.ui.javafx.app.impl;

import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.ui.app.impl.AbstractAtdl4jUserMessageHandler;

import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class JavaFXAtdl4jUserMessageHandler extends AbstractAtdl4jUserMessageHandler {
	private final Logger logger = Logger.getLogger(JavaFXAtdl4jUserMessageHandler.class);

	private Parent parentComposite;

	public void init(Object parentOrShell, Atdl4jOptions atdl4jOptions) {
		init((Pane) parentOrShell, atdl4jOptions);
	}

	public void init(Parent aParentComposite, Atdl4jOptions atdl4jOptions) {
		parentComposite = aParentComposite;
		setAtdl4jOptions(atdl4jOptions);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.atdl4j.ui.app.Atdl4jUserMessageHandler#displayException(java.lang.
	 * String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void displayException(String aTitle, String aMsgText, Throwable e) {
		String txt = "";
		// TODO: remove
		e.printStackTrace();

		if (aTitle != null) {
			txt += aTitle;
			logger.error(aTitle, e);
		} else {
			txt += "Exception";
			logger.error("Exception: ", e);
		}

		String msg = extractExceptionMessage(e);

		if ((aMsgText != null) && (aMsgText.length() > 0)) {
			txt += aMsgText + "\n\n" + msg;
		} else {
			txt += msg;
		}

		Dialog dialog = new Dialog();

		dialog.setTitle(aTitle);
		dialog.getDialogPane().getChildren().add(new Label("Error Message: " + txt));
		dialog.showAndWait();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.atdl4j.ui.app.Atdl4jUserMessageHandler#displayException(java.lang.
	 * String, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void displayMessage(String aTitle, String aMsgText) {
		Dialog<String> messDialog =  new Dialog<String>();
		messDialog.setTitle(aTitle);
		messDialog.getDialogPane().getChildren().add(new Label("INFORMATION_MESSAGE: " + aMsgText));
		messDialog.showAndWait();
	}

}