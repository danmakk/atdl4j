package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.atdl4j.fixatdl.layout.DropDownListT;
import org.atdl4j.fixatdl.layout.EditableDropDownListT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXDropDownListWidget extends AbstractJavaFXWidget<String> {
    
    private ComboBox<String> dropDownList;
    private Label label;
    private Pane wrapper;
    
    @Override
    public void setVisible(boolean visible) {
        if (wrapper != null) {
            wrapper.setVisible(visible);
        } else {
            super.setVisible(visible);
        }
    }
    
    @Override
    protected List<ListItemT> getListItems() {
        return (control instanceof EditableDropDownListT)
                ? ((EditableDropDownListT) control).getListItem() : ((DropDownListT) control).getListItem();
    }
    
    @Override
    public String getControlValueRaw() {
        int selection = dropDownList.getSelectionModel().getSelectedIndex();
        if (selection >= 0) {
            return getListItems().get(selection).getEnumID();
        } else if (control instanceof EditableDropDownListT
                && dropDownList.getSelectionModel().getSelectedItem() != null) {
            if (!"".equals((String) dropDownList.getSelectionModel().getSelectedItem())) {
                // use the enumID if the text matches a combo box item,
                // even if the dropdown was not used to select it
                for (int i = 0; i < dropDownList.getItems().size(); i++) {
                    if (((String) dropDownList.getItems().get(i)).equals((String) dropDownList.getSelectionModel().getSelectedItem())) {
                        return getListItems().get(i + 1).getEnumID();
                    }
                }
                // else use the manually entered text string
                return (String) dropDownList.getSelectionModel().getSelectedItem();
            }
        }
        return null;
    }
    
    @Override
    public String getParameterValue() {
        int selection = dropDownList.getSelectionModel().getSelectedIndex();
        if (selection >= 0) {
            return getParameterValueAsEnumWireValue();
        }
        if (control instanceof EditableDropDownListT
                && dropDownList.getSelectionModel().getSelectedItem() != null
                && dropDownList.getSelectionModel().getSelectedItem() != "") {
            // use the Parameter's Enum wireValue if the text matches a combo box
            // item, even if the dropdown was not used to select it
            for (int i = 0; i < dropDownList.getItems().size(); i++) {
                if (dropDownList.getItems().get(i).equals((String) dropDownList.getSelectionModel().getSelectedItem())) {
                    return getEnumWireValue(getListItems().get(i + 1).getEnumID());
                }
            }
            // else use the manually entered text string
            return (String) dropDownList.getSelectionModel().getSelectedItem();
        }
        return null;
    }
    
    @Override
    protected void processReinit(Object aControlInitValue) {
        if (dropDownList != null) {
            if (aControlInitValue != null) {
                // -- apply initValue if one has been specified --
                setValue((String) aControlInitValue, true);
            } else {
                // -- set to first when no initValue exists --
                if (dropDownList.getItems().size() > 0) {
                    dropDownList.getSelectionModel().select(0);
                }
            }
        }
    }
    
    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //to do 
    }
    
    @Override
    public void setValue(String value) {
        this.setValue(value, false);
    }
    
    public void setValue(String value, boolean setValueAsControl) {
        for (int i = 0; i < getListItems().size(); i++) {
            if (setValueAsControl || parameter == null) {
                if (getListItems().get(i).getEnumID().equals(value)) {
                    dropDownList.getSelectionModel().select(i);
                    break;
                }
            } else {
                if (parameter.getEnumPair().get(i).getWireValue().equals(value)) {
                    String enumID = parameter.getEnumPair().get(i).getEnumID();
                    for (int a = 0; a < getListItems().size(); a++) {
                        if (getListItems().get(a).getEnumID().equals(enumID)) {
                            dropDownList.getSelectionModel().select(a);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        if (label != null) {
            widgets.add(label);
        }
        widgets.add(dropDownList);
        return widgets;
    }
    
    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(dropDownList);
        return widgets;
    }
    
    @Override
    public void addListener(JavaFXListener listener) {
        dropDownList.setOnAction(listener);
        dropDownList.getSelectionModel().selectedItemProperty().addListener(listener);
    }
    
    @Override
    public void removeListener(JavaFXListener listener) {
        dropDownList.setOnAction(null);
        dropDownList.getSelectionModel().selectedItemProperty().removeListener(listener);
    }
    
    @Override
    public void applyConstOrInitValues() {
        super.applyConstOrInitValues();
        dropDownList.setMaxSize(dropDownList.getPrefWidth() + 5, dropDownList.getPrefHeight());
    }
    
    @Override
    protected List< ? extends Node> createBrickComponents() {
        ArrayList<Node> components = new ArrayList<Node>();
        
        String tooltip = getTooltip();

        // label
        if (control.getLabel() != null) {
            label = new Label();
            label.setPadding(new Insets(0, 5, 5, 5));
            label.setText(control.getLabel());
            if (tooltip != null) {
                label.getTooltip().setText(tooltip);
            }
        }

        // dropDownList
        dropDownList = new ComboBox<String>();
        dropDownList.setPadding(new Insets(0, 5, 5, 5));

        // set editable
        dropDownList.setEditable(control instanceof EditableDropDownListT);

        // dropDownList items
        List<ListItemT> listItems = (control instanceof EditableDropDownListT) ? ((EditableDropDownListT) control).getListItem()
                : ((DropDownListT) control).getListItem();
        // TODO: throw error if there are no list items
        for (ListItemT listItem : listItems) {
            dropDownList.getItems().add(listItem.getUiRep() != null ? listItem.getUiRep() : "");
        }

        // tooltip
        if (tooltip != null) {
            dropDownList.getTooltip().setText(tooltip);
        }
        
        dropDownList.setConverter(new StringConverter<String>() {
            
            @Override
            public String toString(String object) {
                return object;
            }
            
            @Override
            public String fromString(String string) {
                return null;
            }
        });
        
        dropDownList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new StrategyParameterListCell();
            }
        });

        // default initializer
        dropDownList.getSelectionModel().select(0);
        dropDownList.setMinWidth(200);

        // select initValue if available
        String initValue = (String) ControlHelper.getInitValue(control, getAtdl4jOptions());
        if (initValue != null) {
            setValue(initValue, true);
        }
        
        if (label != null) {
            components.add(label);
        }
        components.add(dropDownList);
        
        return components;
    }
    
    class StrategyParameterListCell extends ListCell<String> {
        
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item);
            }
        }
    }
}
