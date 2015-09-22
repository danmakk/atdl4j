package org.atdl4j.ui.javafx.config;

import org.atdl4j.config.AbstractAtdl4jConfiguration;

public class JavaFXAtdl4jConfiguration extends AbstractAtdl4jConfiguration {
	private static String PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX = "org.atdl4j.ui.javafx.";

	public JavaFXAtdl4jConfiguration()
	{
		super();
	}

	protected String getDefaultClassNameStrategiesUI() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "impl.JavaFXStrategiesUI";
	}

	protected String getDefaultClassNameStrategyUI() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "impl.JavaFXStrategyUI";
	}

	protected String getDefaultClassNameFixatdlFileSelectionPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXFixatdlFileSelectionPanel";
	}

	protected String getDefaultClassNameFixMsgLoadPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXFixMsgLoadPanel";
	}

	protected String getDefaultClassNameStrategySelectionPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXStrategySelectionPanel";
	}

	protected String getDefaultClassNameStrategyDescriptionPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXStrategyDescriptionPanel";
	}

	protected String getDefaultClassNameAtdl4jTesterPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXAtdl4jTesterPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataSelectionPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXAtdl4jInputAndFilterDataSelectionPanel";
	}

	protected String getDefaultClassNameAtdl4jInputAndFilterDataPanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXAtdl4jInputAndFilterDataPanel";
	}

	protected String getDefaultClassNameAtdl4jCompositePanel() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXAtdl4jCompositePanel";
	}

	protected String getDefaultClassNameAtdl4jUserMessageHandler() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "app.impl.JavaFXAtdl4jUserMessageHandler";
	}

	protected String getDefaultClassNameAtdl4jWidgetForCheckBoxListT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXCheckBoxListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForCheckBoxT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXButtonWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForClockT() {
		// return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXClockWidget";
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXJideClockWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForDoubleSpinnerT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXSpinnerWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForDropDownListT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXDropDownListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForEditableDropDownListT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXDropDownListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForHiddenFieldT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXHiddenFieldWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForLabelT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXLabelWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForMultiSelectListT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXListBoxWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForRadioButtonListT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXRadioButtonListWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForRadioButtonT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXButtonWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSingleSelectListT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXListBoxWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSingleSpinnerT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXSpinnerWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForSliderT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXSliderWidget";
	}

	protected String getDefaultClassNameAtdl4jWidgetForTextFieldT() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "widget.JavaFXTextFieldWidget";
	}

	protected String getDefaultClassNameStrategyPanelHelper() {
		return PACKAGE_PATH_ORG_ATDL4J_UI_JavaFX + "impl.JavaFXStrategyPanelHelper";
	}
}