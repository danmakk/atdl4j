package org.atdl4j.ui.javafx.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.fixatdl.core.BooleanT;
import org.atdl4j.fixatdl.layout.CheckBoxT;
import org.atdl4j.fixatdl.layout.RadioButtonT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXButtonWidget extends AbstractJavaFXWidget<Boolean> {

    private CheckBox button = null;
    private RadioButton button2 = null;

    @Override
    protected void processReinit(Object aControlInitValue) {
        if (button != null) {
            button.setSelected((aControlInitValue != null) ? ((Boolean) aControlInitValue) : false);
        } else if (button2 != null) {
            button2.setSelected((aControlInitValue != null) ? ((Boolean) aControlInitValue) : false);
        }

    }

    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //to do 
    }

    // Parameter value looks up checkedEnumRef and uncheckedEnumRef
    @Override
    public Object getParameterValue() {
        if (getControlValue() == null) {
            return null;
        } else if (parameter instanceof BooleanT) {
            return getControlValue(); // Short-circuit for Boolean parameters
        } else if (getControlValue().equals(Boolean.TRUE)) {
            String checkedEnumRef = control instanceof RadioButtonT ? ((RadioButtonT) control).getCheckedEnumRef() : ((CheckBoxT) control).getCheckedEnumRef();
            if (checkedEnumRef != null && !checkedEnumRef.equals("")) {
                if (checkedEnumRef.equals(Atdl4jConstants.VALUE_NULL_INDICATOR)) {
                    return null;
                } else {
                    return getEnumWireValue(checkedEnumRef);
                }
            } else {
                return Boolean.TRUE;
            }
        } else if (getControlValue().equals(Boolean.FALSE)) {
            String uncheckedEnumRef = control instanceof RadioButtonT ? ((RadioButtonT) control).getUncheckedEnumRef() : ((CheckBoxT) control)
                    .getUncheckedEnumRef();
            if (uncheckedEnumRef != null && !uncheckedEnumRef.equals("")) {
                if (uncheckedEnumRef.equals(Atdl4jConstants.VALUE_NULL_INDICATOR)) {
                    return null;
                } else {
                    return getEnumWireValue(uncheckedEnumRef);
                }
            } else {
                return Boolean.FALSE;
            }
        }
        return null;
    }

    @Override
    public Boolean getControlValueRaw() {
        if (button != null) {
            return button.isSelected();
        } else if (button2 != null) {
            return button2.isSelected();
        }
        return false;
    }

    @Override
    public void setValue(Boolean value) {
        if (button != null) {
            if (button.isSelected() != value) {
                button.setSelected(value);
            } else {
                button.setDisable(value);
            }
        } else if (button2 != null) {
            if (button2.isSelected() != value) {
                button2.setSelected(value);
            } else {
                button2.setDisable(!value);
            }
        }
    }

    public ButtonBase getButton() {
        if (button != null) {
            return button;
        } else if (button2 != null) {
            return button2;
        }
        return null;
    }
    
    public RadioButton getRadioButton(){
        return button2;
    }

    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        if (button != null) {
            widgets.add(button);
        } else if (button2 != null) {
            widgets.add(button2);
        }
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        return getComponents();
    }

    @Override
    public void addListener(JavaFXListener listener) {
        if (button != null) {
            button.setOnAction(listener);
        } else if (button2 != null) {
            button2.setOnAction(listener);
        }
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        if (button != null) {
            button.setOnAction(null);
        } else if (button2 != null) {
            button2.setOnAction(null);
        }
    }

    @Override
    public List<? extends Node> createBrickComponents() {
        
        if (control instanceof RadioButtonT) {
            button2 = new RadioButton(control.getLabel());
            button2.setPadding(new Insets(5, 10, 5, 5));
            button2.setId(control.getParameterRef());
            button = null;
        } else if (control instanceof CheckBoxT) {
            button = new CheckBox(control.getLabel());
            button.setPadding(new Insets(5, 10, 5, 5));
            button.setId(control.getParameterRef());
            button2 = null;
        }

        if (button != null) {
            if (getTooltip() != null) {
                button.setTooltip(new Tooltip(getTooltip()));
            }
        } else if (button2 != null) {
            if (getTooltip() != null) {
                button2.setTooltip(new Tooltip(getTooltip()));
            }
        }

        Boolean tempInitValue = (Boolean) ControlHelper.getInitValue(control, getAtdl4jOptions());

        if (tempInitValue != null) {
            setValue(tempInitValue);
        }

        if (control instanceof RadioButtonT) {
            return Collections.singletonList(button2);
        } else if (control instanceof CheckBoxT) {
            return Collections.singletonList(button);
        }
        return null;
    }

}
