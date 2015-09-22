package org.atdl4j.ui.javafx.app.impl;

import org.atdl4j.config.Atdl4jOptions;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.atdl4j.ui.app.impl.AbstractStrategyDescriptionPanel;

public class JavaFXStrategyDescriptionPanel extends AbstractStrategyDescriptionPanel {

    // JPanel as we need TitledBorder private Container container;

    private Pane container;

    private WebView strategyDescription;

    @Override
    public Object buildStrategyDescriptionPanel(Object parentOrShell, Atdl4jOptions atdl4jOptions) {
        return buildStrategyDescriptionPanel((Parent) parentOrShell, atdl4jOptions);
    }

    public Pane buildStrategyDescriptionPanel(Parent aParentContainer, Atdl4jOptions atdl4jOptions) {
        setAtdl4jOptions(atdl4jOptions);

        VBox tmp = new VBox();
        tmp.getChildren().add(new Label("Strategy Description"));

        strategyDescription = new WebView();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(strategyDescription);
        tmp.getChildren().add(scrollPane);
        container = tmp;
        return container;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.AbstractStrategyDescriptionPanel#
     * setStrategyDescriptionText (java.lang.String)
     */
    protected void setStrategyDescriptionText(String aText) {
        if (strategyDescription != null) {
            String newText = aText.replaceAll("&apos;", "&#39;");
            String htmlText = "<html><body face=\"arial\">" + newText + "</body></html>";

            WebEngine webEngine = strategyDescription.getEngine();
            webEngine.loadContent(htmlText);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.StrategyDescriptionPanel#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean aVisible) {
        if (container != null) {
            container.setVisible(aVisible);
        }
    }
}
