package org.atdl4j.ui.javafx.app.impl;

import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.atdl4j.config.Atdl4jOptions;
import org.atdl4j.config.StrategyFilterInputData;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.ui.app.impl.AbstractAtdl4jInputAndFilterDataPanel;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class JavaFXAtdl4jInputAndFilterDataPanel extends AbstractAtdl4jInputAndFilterDataPanel {
    
    public final Logger logger = Logger.getLogger(JavaFXAtdl4jInputAndFilterDataPanel.class);
    
    private Dialog parentDialog;
    
    private ComboBox<String> strategyFilterFixMsgTypeCombo;
    private CheckBox checkboxInputCxlReplaceMode;
    private ComboBox<String> strategyFilterRegionCombo;
    private ComboBox<String> strategyFilterCountryCombo;
    private ComboBox<String> strategyFilterMICCodeCombo;
    private ComboBox<String> strategyFilterSecurityTypeCombo;
    
    private ComboBox<String> fixFieldOrdTypeCombo;
    private ComboBox<String> fixFieldSideCombo;
    private ComboBox<String> fixFieldOrderQtyCombo;
    private ComboBox<String> fixFieldPriceCombo;
    private ComboBox<String> fixFieldHandlInstCombo;
    private ComboBox<String> fixFieldExecCombo;
    private ComboBox<String> fixFieldTimeInForceCombo;
    private ComboBox<String> fixFieldClOrdLinkIDCombo;

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel#
     * buildAtdl4jInputAndFilterDataPanel(java.lang.Object,
     * org.atdl4j.config.Atdl4jOptions)
     */
    @Override
    public Object buildAtdl4jInputAndFilterDataPanel(Object aParentOrShell, Atdl4jOptions aAtdl4jOptions) {
        return buildAtdl4jInputAndFilterDataPanel((Dialog) aParentOrShell, aAtdl4jOptions);
    }
    
    public Dialog buildAtdl4jInputAndFilterDataPanel(Dialog aParentOrShell, Atdl4jOptions aAtdl4jOptions) {
        parentDialog = (Dialog) aParentOrShell;

        // -- Delegate back to AbstractAtdl4jInputAndFilterDataPanel --
        init(aParentOrShell, aAtdl4jOptions);

        // -- Build the JavaFX.JPanel from Atdl4jCompositePanel --
        buildCoreAtdl4jSettingsPanel(aParentOrShell);
        
        return parentDialog;
    }
    
    protected Pane buildCoreAtdl4jSettingsPanel(Dialog aParentOrShell) {
        BorderPane tempPanel = new BorderPane();
        
        tempPanel.setCenter(buildStandardFixFieldsPanel());
        tempPanel.setTop(buildStrategyFilterPanel());
        
        aParentOrShell.getDialogPane().getChildren().add(tempPanel);
        
        return tempPanel;
    }
    
    protected Pane buildStrategyFilterPanel() {
        Pane strategyFilterPanel = new AnchorPane();
        
        VBox filterPanelLayout = new VBox();
        
        Label borderTitleName = new Label(STRATEGY_FILTER_PANEL_NAME);
        
        strategyFilterPanel.getChildren().addAll(borderTitleName, filterPanelLayout);
        
        Pane panel1 = new AnchorPane();
        
        panel1.getChildren().add(new Label("FixMsgType:"));
        strategyFilterFixMsgTypeCombo = new ComboBox<String>(
                convertToObservableList(prepareContantsForGUI(Atdl4jConstants.STRATEGY_FILTER_FIX_MSG_TYPES)));
        strategyFilterFixMsgTypeCombo.getSelectionModel().select(0);
        panel1.getChildren().add(strategyFilterFixMsgTypeCombo);
        
        checkboxInputCxlReplaceMode = new CheckBox("Cxl Replace Mode");
        panel1.getChildren().add(checkboxInputCxlReplaceMode);
        
        Pane panel2 = new AnchorPane();
        panel2.getChildren().add(new Label("Region:"));
        strategyFilterRegionCombo = new ComboBox<String>(
                convertToObservableList(prepareContantsForGUI(Atdl4jConstants.STRATEGY_FILTER_REGIONS)));
        strategyFilterFixMsgTypeCombo.getSelectionModel().select(0);
        panel2.getChildren().add(strategyFilterRegionCombo);
        
        panel2.getChildren().add(new Label("Country:"));
        strategyFilterCountryCombo = new ComboBox<String>(convertToObservableList(DEFAULT_STRATEGY_FILTER_COUNTRY_SUBSET_LIST));
        strategyFilterCountryCombo.setEditable(true);
        strategyFilterCountryCombo.getSelectionModel().select(0);
        panel2.getChildren().add(strategyFilterCountryCombo);
        
        panel2.getChildren().add(new Label("MIC Code:"));
        strategyFilterMICCodeCombo = new ComboBox<String>(convertToObservableList(DEFAULT_STRATEGY_FILTER_MIC_CODE_SUBSET_LIST));
        strategyFilterMICCodeCombo.setEditable(true);
        strategyFilterMICCodeCombo.getSelectionModel().select(0);
        panel2.getChildren().add(strategyFilterMICCodeCombo);
        
        panel2.getChildren().add(new Label("Security Type:"));
        strategyFilterSecurityTypeCombo = new ComboBox<String>(
                convertToObservableList(prepareContantsForGUI(Atdl4jConstants.STRATEGY_FILTER_SECURITY_TYPES)));
        strategyFilterSecurityTypeCombo.setEditable(true);
        strategyFilterSecurityTypeCombo.getSelectionModel().select(0);
        panel2.getChildren().add(strategyFilterSecurityTypeCombo);
        
        strategyFilterPanel.getChildren().addAll(panel1, panel2);
        
        return strategyFilterPanel;
    }
    
    protected Pane buildStandardFixFieldsPanel() {
        AnchorPane standardFixFieldsPanelContainer = new AnchorPane();
        GridPane standardFixFieldsPanel = new GridPane();
        
        standardFixFieldsPanelContainer.getChildren().addAll(new Label(STANDARD_FIX_FIELDS_PANEL_NAME),
                standardFixFieldsPanel);
        
        standardFixFieldsPanel.getChildren().add(new Label("OrdType:"));
        fixFieldOrdTypeCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_ORD_TYPE_SUBSET_LIST));
        fixFieldOrdTypeCombo.setEditable(true);
        fixFieldOrdTypeCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldOrdTypeCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("Side:"));
        fixFieldSideCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_SIDE_SUBSET_LIST));
        fixFieldSideCombo.setEditable(true);
        fixFieldSideCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldSideCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("OrderQty:"));
        fixFieldOrderQtyCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_ORDER_QTY_SUBSET_LIST));
        fixFieldOrderQtyCombo.setEditable(true);
        fixFieldOrderQtyCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldOrderQtyCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("Price:"));
        fixFieldPriceCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_PRICE_SUBSET_LIST));
        fixFieldPriceCombo.setEditable(true);
        fixFieldPriceCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldPriceCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("HandlInst:"));
        fixFieldHandlInstCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_PRICE_SUBSET_LIST));
        fixFieldHandlInstCombo.setEditable(true);
        fixFieldHandlInstCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldHandlInstCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("ExecInst:"));
        fixFieldExecCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_EXEC_INST_SUBSET_LIST));
        fixFieldExecCombo.setEditable(true);
        fixFieldExecCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldExecCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("TimeInForce:"));
        fixFieldTimeInForceCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_TIME_IN_FORCE_SUBSET_LIST));
        fixFieldTimeInForceCombo.setEditable(true);
        fixFieldTimeInForceCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldTimeInForceCombo);
        
        standardFixFieldsPanel.getChildren().add(new Label("ClOrdLinkID:"));
        fixFieldClOrdLinkIDCombo = new ComboBox<String>(convertToObservableList(DEFAULT_FIX_FIELD_TIME_IN_FORCE_SUBSET_LIST));
        fixFieldClOrdLinkIDCombo.setEditable(true);
        fixFieldClOrdLinkIDCombo.getSelectionModel().select(0);
        standardFixFieldsPanel.getChildren().add(fixFieldClOrdLinkIDCombo);
        
        return standardFixFieldsPanel;
    }
    
    private ObservableList<String> convertToObservableList(String[] items) {
        ObservableList<String> convertedItems = FXCollections.observableArrayList();
        convertedItems.addAll(Arrays.asList(items));
        return convertedItems;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel#
     * extractAtdl4jOptionsFromScreen()
     */
    @Override
    public boolean extractAtdl4jOptionsFromScreen() {
        StrategyFilterInputData tempStrategyFilterInputData = new StrategyFilterInputData();
        
        tempStrategyFilterInputData.setFixMsgType(getDropDownItemSelected(strategyFilterFixMsgTypeCombo));
        tempStrategyFilterInputData.setRegion_name(getDropDownItemSelected(strategyFilterRegionCombo));
        tempStrategyFilterInputData.setCountry_CountryCode(getDropDownItemSelected(strategyFilterCountryCombo));
        if ((tempStrategyFilterInputData.getCountry_CountryCode() != null)
                && (tempStrategyFilterInputData.getRegion_name() == null)) {
            throw new IllegalArgumentException("Region is required when Country is specified.");
        }
        
        tempStrategyFilterInputData.setMarket_MICCode(getDropDownItemSelected(strategyFilterMICCodeCombo));
        tempStrategyFilterInputData.setSecurityType_name(getDropDownItemSelected(strategyFilterSecurityTypeCombo));

        // -- Set the StrategyFilterInputData we just built --
        getAtdl4jOptions().getInputAndFilterData().setStrategyFilterInputData(tempStrategyFilterInputData);
        
        getAtdl4jOptions().getInputAndFilterData().setInputCxlReplaceMode(checkboxInputCxlReplaceMode.isSelected());
        
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_ORD_TYPE, fixFieldOrdTypeCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_SIDE, fixFieldSideCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_ORDER_QTY, fixFieldOrderQtyCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_PRICE, fixFieldPriceCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_HANDL_INST, fixFieldHandlInstCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_EXEC_INST, fixFieldExecCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_TIME_IN_FORCE, fixFieldTimeInForceCombo);
        addFixFieldToInputAndFilterData(FIX_FIELD_NAME_CL_ORD_LINK_ID, fixFieldClOrdLinkIDCombo);
        
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.atdl4j.ui.app.Atdl4jInputAndFilterDataPanel#
     * loadScreenWithAtdl4jOptions()
     */
    @Override
    public boolean loadScreenWithAtdl4jOptions() {
        
        if (getAtdl4jOptions().getInputAndFilterData() != null) {
            StrategyFilterInputData tempStrategyFilterInputData = null;
            if ((getAtdl4jOptions().getInputAndFilterData().getStrategyFilterInputDataList() != null)
                    && (getAtdl4jOptions().getInputAndFilterData().getStrategyFilterInputDataList().size() > 0)) {
                tempStrategyFilterInputData = getAtdl4jOptions().getInputAndFilterData()
                        .getStrategyFilterInputDataList().get(0);
            }
            
            String tempFixMsgType = null;
            String tempRegion_name = null;
            String tempCountry_CountryCode = null;
            String tempMarket_MICCode = null;
            String tempSecurityType_name = null;
            
            if (tempStrategyFilterInputData != null) {
                tempFixMsgType = tempStrategyFilterInputData.getFixMsgType();
                tempRegion_name = tempStrategyFilterInputData.getRegion_name();
                tempCountry_CountryCode = tempStrategyFilterInputData.getCountry_CountryCode();
                tempMarket_MICCode = tempStrategyFilterInputData.getMarket_MICCode();
                tempSecurityType_name = tempStrategyFilterInputData.getSecurityType_name();
            }
            
            selectDropDownItem(strategyFilterFixMsgTypeCombo, tempFixMsgType);
            selectDropDownItem(strategyFilterRegionCombo, tempRegion_name);
            selectDropDownItem(strategyFilterCountryCombo, tempCountry_CountryCode);
            selectDropDownItem(strategyFilterMICCodeCombo, tempMarket_MICCode);
            selectDropDownItem(strategyFilterSecurityTypeCombo, tempSecurityType_name);
            
            setCheckboxValue(checkboxInputCxlReplaceMode,
                    getAtdl4jOptions().getInputAndFilterData().getInputCxlReplaceMode(), Boolean.FALSE);
            
            selectDropDownItem(fixFieldOrdTypeCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_ORD_TYPE));
            selectDropDownItem(fixFieldSideCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_SIDE));
            selectDropDownItem(fixFieldOrderQtyCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_ORDER_QTY));
            selectDropDownItem(fixFieldPriceCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_PRICE));
            selectDropDownItem(fixFieldHandlInstCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_HANDL_INST));
            selectDropDownItem(fixFieldExecCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_EXEC_INST));
            selectDropDownItem(fixFieldTimeInForceCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_TIME_IN_FORCE));
            selectDropDownItem(fixFieldClOrdLinkIDCombo,
                    getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldValue(FIX_FIELD_NAME_CL_ORD_LINK_ID));
            return true;
        } else {
            return false;
        }
    }
    
    public static void setCheckboxValue(CheckBox aCheckbox, Boolean aValue, boolean aStateIfNull) {
        if (aValue == null) {
            aCheckbox.setSelected(aStateIfNull);
        } else {
            aCheckbox.setSelected(aValue);
        }
    }
    
    public static String getDropDownItemSelected(ComboBox<String> aDropDown) {
        String tempText = (String) aDropDown.getSelectionModel().getSelectedItem();
        if ("".equals(tempText)) {
            return null;
        } else {
            return tempText;
        }
    }
    
    public static void selectDropDownItem(ComboBox<String> aDropDown, String aItem) {
        if (aItem != null) {
            aDropDown.getSelectionModel().select(aItem);
        } else {
            aDropDown.getSelectionModel().select(0);
        }
    }
    
    protected void addFixFieldToInputAndFilterData(String aFieldName, ComboBox<String> aDropDown) {
        String tempFieldValue = getDropDownItemSelected(aDropDown);
        if (tempFieldValue != null) {
            // -- Add/update it --
            getAtdl4jOptions().getInputAndFilterData().setInputHiddenFieldNameValuePair(aFieldName, tempFieldValue);
        } else {
            if ((getAtdl4jOptions() != null) && (getAtdl4jOptions().getInputAndFilterData() != null)
                    && (getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldNameValueMap() != null)) {
                // -- Attempt to remove it if it exists --
                getAtdl4jOptions().getInputAndFilterData().getInputHiddenFieldNameValueMap().remove(aFieldName);
            }
        }
    }
    
    private String[] prepareContantsForGUI(String[] constants) {
        if (constants.length == 0) {
            return constants;
        }
        String[] val = new String[constants.length + 1];
        val[0] = "";
        
        System.arraycopy(constants, 0, val, 1, constants.length);
        return val;
    }
}
