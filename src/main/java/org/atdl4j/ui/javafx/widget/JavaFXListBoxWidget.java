package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.MultiSelectListT;
import org.atdl4j.fixatdl.layout.SingleSelectListT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 * @author daniel.makgonta
 */
public class JavaFXListBoxWidget extends AbstractJavaFXWidget<String> {

    private ListView listBox;
    private Label label;
    private ObservableList<String> list = FXCollections.observableArrayList();

    @Override
    protected void processReinit(Object aControlInitValue) {
        if (listBox != null) {
            if (aControlInitValue != null) {
                // -- apply initValue if one has been specified --
                setValue((String) aControlInitValue, true);
            } else {
                // -- set to first when no initValue exists --
                if (list.size() > 0) {
                    listBox.getSelectionModel().selectFirst();
                }
            }
        }
    }

    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //do nothing
    }

    @Override
    public String getControlValueRaw() {
        String value = "";
        List<ListItemT> listItems = control instanceof MultiSelectListT ? ((MultiSelectListT) control).getListItem()
                : ((SingleSelectListT) control).getListItem();
        Integer[] selection = (Integer[]) listBox.getSelectionModel().getSelectedIndices().toArray();

        for (int i = 0; i < selection.length; i++) {
            value += listItems.get(selection[i]).getEnumID();
            if (i + 1 != selection.length) {
                value += " ";
            }
        }
        return "".equals(value) ? null : value;
    }

    @Override
    public String getParameterValue() {
        // Helper method from AbstractControlUI
        return getParameterValueAsMultipleValueString();
    }

    @Override
    public void setValue(String value) {
        this.setValue(value, false);
    }

    public void setValue(String value, boolean setValueAsControl) {
        // split string by spaces in case of MultiSelectList
        List<String> values = Arrays.asList(value.split("\\s"));
        for (String singleValue : values) {
            for (int i = 0; i < getListItems().size(); i++) {
                if (setValueAsControl || parameter == null) {
                    if (getListItems().get(i).getEnumID().equals(singleValue)) {
                        listBox.getSelectionModel().select(i);
                        break;
                    }
                } else {
                    if (parameter.getEnumPair().get(i).getWireValue().equals(singleValue)) {
                        listBox.getSelectionModel().select(i);
                        break;
                    }
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
        widgets.add(listBox);
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(listBox);
        return widgets;
    }

    @Override
    public void addListener(JavaFXListener listener) {
        listBox.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        listBox.getSelectionModel().selectedItemProperty().removeListener(listener);
    }

    @Override
    public void applyConstOrInitValues() {
        super.applyConstOrInitValues();
    }

    protected List< ? extends Node> createBrickComponents() {

        List<Node> components = new ArrayList<Node>();

        String tooltip = getTooltip();

        // label
        if (control.getLabel() != null) {
            label = new Label();
            label.setText(control.getLabel());
            label.setPadding(new Insets(0, 5, 5, 5));
            if (tooltip != null) {
                label.getTooltip().setText(tooltip);
            }
            components.add(label);
        }

        // listbox
        listBox = new ListView<String>(list);
        listBox.setPadding(new Insets(0, 5, 5, 5));

        if (control instanceof MultiSelectListT) {
            listBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        } else if (control instanceof SingleSelectListT) {
            listBox.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }

        // listBox items
        java.util.List<ListItemT> listItems = control instanceof MultiSelectListT ? ((MultiSelectListT) control).getListItem()
                : ((SingleSelectListT) control).getListItem();
        for (ListItemT listItem : listItems) {
            list.add(listItem.getUiRep() != null ? listItem.getUiRep() : "");
        }

        // tooltip
        if (tooltip != null) {
            Tooltip tip = new Tooltip(tooltip);
            listBox.setTooltip(tip);
        }

        // init value
        String initValue = (String) ControlHelper.getInitValue(control, getAtdl4jOptions());
        if (initValue != null) {
            setValue(initValue, true);
        }

        return components;
    }
}
