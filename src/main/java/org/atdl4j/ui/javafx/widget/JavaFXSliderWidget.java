package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;
import org.atdl4j.fixatdl.core.EnumPairT;
import org.atdl4j.fixatdl.layout.ListItemT;
import org.atdl4j.fixatdl.layout.SliderT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXSliderWidget extends AbstractJavaFXWidget<String> {
    
    private Slider slider;
    private Label label;
    
    @Override
    protected void processReinit(Object aControlInitValue) {
        if ((slider != null)) {
            if (aControlInitValue != null) {
                // -- apply initValue if one has been specified --
                setValue((String) aControlInitValue, true);
            } else {
                // -- set to minimum when no initValue exists --
                slider.setValue(slider.getMin());
            }
        }
    }
    
    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //do nothing
    }
    
    @Override
    public String getControlValueRaw() {
        return ((SliderT) control).getListItem().get((int) slider.getValue()).getEnumID();
    }
    
    @Override
    public void setValue(String value) {
        this.setValue(value, false);
    }
    
    public void setValue(String value, boolean setValueAsControl) {
        for (int i = 0; i < getListItems().size(); i++) {
            if (setValueAsControl || parameter == null) {
                if (getListItems().get(i).getEnumID().equals(value)) {
                    slider.setValue(i);
                    break;
                }
            } else {
                if (parameter.getEnumPair().get(i).getWireValue().equals(value)) {
                    slider.setValue(i);
                    break;
                }
            }
        }
    }
    
    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(label);
        widgets.add(slider);
        return widgets;
    }
    
    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(slider);
        return widgets;
    }
    
    @Override
    public void addListener(JavaFXListener listener) {
        slider.valueProperty().addListener(listener);
    }
    
    @Override
    public void removeListener(JavaFXListener listener) {
        slider.valueProperty().removeListener(listener);
    }
    
    @Override
    protected List< ? extends Node> createBrickComponents() {
        List<Node> components = new ArrayList<Node>();

        //  label
        label = new Label();
        label.setPadding(new Insets(0, 5, 5, 5));
        if (control.getLabel() != null) {
            label.setText(control.getLabel());
        }
        
        int numColumns = ((SliderT) control).getListItem().size();

        // slider
        slider = new Slider(0, numColumns - 1, 0);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setPadding(new Insets(0, 5, 5, 5));
        
        // add major tick marks
        slider.setMajorTickUnit(1);

        // labels based on parameter ListItemTs
        //  if ( ( (SliderT) control ).getListItem() != null )
        //  {
        Hashtable<Integer, Label> labelTable = new Hashtable<Integer, Label>();
        int i = 0;
        for (ListItemT li : ((SliderT) control).getListItem()) {
            Label label = new Label();
            if (li.getUiRep() != null && !li.getUiRep().equals("")) {
                label.setText(li.getUiRep());
            }
            for (EnumPairT ep : parameter.getEnumPair()) {
                if (ep.getEnumID().equals(li.getEnumID()) && ep.getDescription() != null && !ep.getDescription().equals("")) {
                    label.getTooltip().setText(ep.getDescription());
                }
            }
            labelTable.put(new Integer(i), label);
            i++;
        }
        
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        
        slider.setLabelFormatter(new StringConverter<Double>() {
            
            @Override
            public String toString(Double object) {
                return String.valueOf(object);
            }
            
            @Override
            public Double fromString(String string) {
                return Double.parseDouble(string);
            }
        });
  //  }

        // tooltip
        if (getTooltip() != null) {
            Tooltip tip = new Tooltip(getTooltip());
            slider.setTooltip(tip);
            if (label != null) {
                label.setTooltip(tip);
            }
        }
        
        if (ControlHelper.getInitValue(control, getAtdl4jOptions()) != null) {
            setValue((String) ControlHelper.getInitValue(control, getAtdl4jOptions()), true);
        }

        //TODO: make this a composite
        components.add(label);
        components.add(slider);
        
        return components;
    }
}
