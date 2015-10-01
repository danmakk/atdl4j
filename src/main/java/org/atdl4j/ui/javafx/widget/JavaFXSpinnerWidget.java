package org.atdl4j.ui.javafx.widget;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.atdl4j.data.converter.DecimalConverter;
import org.atdl4j.data.converter.IntegerConverter;
import org.atdl4j.fixatdl.layout.DoubleSpinnerT;
import org.atdl4j.fixatdl.layout.SingleSpinnerT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXSpinnerWidget extends AbstractJavaFXWidget<BigDecimal> {

    private static final BigDecimal DEFAULT_INNER_INCREMENT = new BigDecimal(1);
    private static final BigDecimal DEFAULT_OUTER_INCREMENT = new BigDecimal(1);
    private static final long serialVersionUID = 2947835451995064559L;
    public static BigDecimal MIN_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal(-Integer.MAX_VALUE);
    public static BigDecimal MAX_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal(Integer.MAX_VALUE);
    private Spinner spinner;
    private Label label;
    private int digits = 0;
    private VBox wrapper;
    private SpinnerValueFactory model;


    @Override
    protected void processReinit(Object aControlInitValue) {
        if (spinner != null) {
            applyInitialValue();
        }
    }

    public class DoubleSpinnerListener implements EventHandler<ActionEvent>, ChangeListener<Double> {

        private Spinner spinner;
        private BigDecimal increment1;
        private BigDecimal increment2;
        private BigDecimal oldVal;

        public DoubleSpinnerListener(Spinner spinner, BigDecimal increment1, BigDecimal increment2) {
            this.spinner = spinner;
            this.increment1 = increment1;
            this.increment2 = increment2;
        }

        @Override
        public void handle(ActionEvent event) {
            BigDecimal value = (BigDecimal.valueOf((Double) spinner.getValue()));

            if (value == null) {
                value = BigDecimal.ZERO;
            }

            if (oldVal == null) {
                oldVal = BigDecimal.ZERO;
            }

            if (value.compareTo(oldVal) > 0) {
                value.add(increment1);
            } else if (value.compareTo(oldVal) < 0) {
                value.add(increment2);
            }

            oldVal = value;
        }

        @Override
        public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
            if (newValue == null) {
                newValue = BigDecimal.ZERO.doubleValue();
            }

            if (oldValue == null) {
                oldValue = BigDecimal.ZERO.doubleValue();
            }

            if (newValue.compareTo(oldValue) > 0) {
                newValue += increment1.doubleValue();
            } else {
                newValue += increment2.doubleValue();
            }
        }
    }

    /**
     * Invokes spinner.setSelection() assigning - Control/@initValue if non-null
     * - Parameter/@minValue if non-null - otherwise 0
     */
    protected void applyInitialValue() {
        Double initValue = (Double) ControlHelper.getInitValue(control, getAtdl4jOptions());
        if (initValue != null) {
            setValue(new BigDecimal(initValue));
        } else {
            setValue(new BigDecimal(0.0));
        }
    }

    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //do nothing
    }

    @Override
    public BigDecimal getControlValueRaw() {
        Double num = null;
        Integer num2 = null;
        try {
            num = Double.parseDouble(spinner.getValue().toString());
        } catch (NumberFormatException e) {
            num2 = Integer.parseInt(spinner.getValue().toString());
        }

        BigDecimal val;

        if (num != null) {
            val = BigDecimal.valueOf(num);
        } else {
            val = BigDecimal.valueOf((double) num2);
        }

        if (val != null) {
            val = val.setScale(digits, RoundingMode.HALF_UP);
        }
        return val;
    }

    @Override
    public void setValue(BigDecimal value) {
        if (value != null) {
            if (spinner.getValueFactory() instanceof SpinnerValueFactory.IntegerSpinnerValueFactory) {
                spinner.getValueFactory().setValue(value.intValue());
            } else if (spinner.getValueFactory() instanceof SpinnerValueFactory.DoubleSpinnerValueFactory) {
                spinner.getValueFactory().setValue(value.doubleValue());
            }
        }
    }

    @Override
    public void createWidget(Pane parent) {
        List< ? extends Node> components = getBrickComponents();
        if (!components.isEmpty()) {
            if (components.size() > 1) {
                wrapper = new VBox();
                wrapper.getChildren().add(components.get(0));
                wrapper.getChildren().add(components.get(1));
                parent.getChildren().add(wrapper);
            } else {
                parent.getChildren().add(components.get(0));
            }
        }
    }

    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(label);
        widgets.add(spinner);
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        widgets.add(spinner);
        return widgets;
    }

    @Override
    public void addListener(JavaFXListener listener) {
        spinner.valueProperty().addListener(listener);
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        spinner.valueProperty().removeListener(listener);
    }

    @Override
    public void setVisible(boolean visible) {
        if (wrapper != null) {
            wrapper.setVisible(visible);
        } else {
            super.setVisible(visible);
        }
    }

    @Override
    public List< ? extends Node> createBrickComponents() {

        List<Node> components = new ArrayList<Node>();
        String tooltip = getTooltip();

        // label
        if (control.getLabel() != null) {
            label = new Label();
            label.setText(control.getLabel());
            label.setPadding(new Insets(0, 5, 0, 0));
            if (tooltip != null) {
                label.setTooltip(new Tooltip(tooltip));
            }
            components.add(label);
        }

        Node comp = null;
        if (control instanceof SingleSpinnerT) {
            // spinner
            spinner = new Spinner();
            spinner.setId(control.getParameterRef());

            // tooltip
            if (tooltip != null) {
                spinner.setTooltip(new Tooltip(tooltip));
            }
            comp = spinner;
        } else if (control instanceof DoubleSpinnerT) {
            // doubleSpinnerGrid
            spinner = new Spinner();
            HBox w = new HBox();

            // tooltip
            if (tooltip != null) {
                spinner.setTooltip(new Tooltip(tooltip));
            }

            // layout
            w.getChildren().add(spinner);

            comp = w;
        }
        components.add(comp);

        spinner.setEditable(true);

        spinner.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {

                    if (spinner.getValueFactory() instanceof SpinnerValueFactory.IntegerSpinnerValueFactory) {
                        Integer value;
                        try {
                            value = Integer.parseInt(newValue);
                        } catch (NumberFormatException e) {
                            value = 0;
                        }
                        spinner.getValueFactory().setValue(value);
                    } else if (spinner.getValueFactory() instanceof SpinnerValueFactory.DoubleSpinnerValueFactory) {
                        Double value;
                        try {
                            value = Double.parseDouble(newValue);
                        } catch (NumberFormatException e) {
                            value = 0.00;
                        }
                        spinner.getValueFactory().setValue(value);
                    }
                }
            }
        });

        spinner.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String val = spinner.getEditor().getText();
                    spinner.getValueFactory().setValue(val);
                }
            }
        });

        // number model
        model = (control instanceof DoubleSpinnerT)
            ? new SpinnerValueFactory.DoubleSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE)
            : new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinner.setValueFactory(model);

        // Set min/max/precision if a parameter is attached
        if (parameterConverter != null && parameterConverter instanceof DecimalConverter) {
            DecimalConverter tempDecimalConverter = (DecimalConverter) parameterConverter;

            if (tempDecimalConverter.getPrecision() != null) {
                digits = tempDecimalConverter.getPrecision().intValue();
            } else {
                digits = ControlHelper.getDefaultDigitsForSpinnerControl(parameterConverter.getParameter(), getAtdl4jOptions());
            }

            if (model instanceof SpinnerValueFactory.DoubleSpinnerValueFactory) {
                if (tempDecimalConverter.getMinValue() != null) {
                    // -- need to handle Percentage ("control value" representation) --
                    BigDecimal tempParameterMin = tempDecimalConverter.getMinValue();
                    BigDecimal tempControlMin = tempDecimalConverter.convertParameterValueToControlValue(tempParameterMin);
                    ((SpinnerValueFactory.DoubleSpinnerValueFactory) model).setMin(tempControlMin.setScale(digits, RoundingMode.HALF_UP).doubleValue());
                }

                if (tempDecimalConverter.getMaxValue() != null) {
                    // -- need to handle Percentage ("control value" representation) --
                    BigDecimal tempParameterMax = tempDecimalConverter.getMaxValue();
                    BigDecimal tempControlMax = tempDecimalConverter.convertParameterValueToControlValue(tempParameterMax);
                    ((SpinnerValueFactory.DoubleSpinnerValueFactory) model).setMax(tempControlMax.setScale(digits, RoundingMode.HALF_UP).doubleValue());
                }
            }
        } else if (parameterConverter != null && parameterConverter instanceof IntegerConverter) {
            IntegerConverter tempIntegerConverter = (IntegerConverter) parameterConverter;

            // -- Integer always has 0 digits --
            digits = 0;

            if (model instanceof SpinnerValueFactory.IntegerSpinnerValueFactory) {
                if (tempIntegerConverter.getMinValue() != null) {
                    BigInteger tempParameterMin = tempIntegerConverter.getMinValue();
                    BigInteger tempControlMin = tempIntegerConverter.convertParameterValueToControlValue(tempParameterMin);
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setMin(new BigDecimal(tempControlMin).intValue());
                } else {
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setMin(JavaFXNullableSpinner.MIN_INTEGER_VALUE_AS_BIG_DECIMAL.intValue());
                }

                if (tempIntegerConverter.getMaxValue() != null) {
                    BigInteger tempParameterMax = tempIntegerConverter.getMaxValue();
                    BigInteger tempControlMax = tempIntegerConverter.convertParameterValueToControlValue(tempParameterMax);
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setMax(new BigDecimal(tempControlMax).intValue());
                } else {
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setMax(JavaFXNullableSpinner.MAX_INTEGER_VALUE_AS_BIG_DECIMAL.intValue());
                }
            }
        }

        if (control instanceof DoubleSpinnerT) {
            BigDecimal tempInnerIncrement = ControlHelper.getInnerIncrementValue(control, getAtdl4jOptions(), digits);
            if (tempInnerIncrement != null) {
                // -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
                ((SpinnerValueFactory.DoubleSpinnerValueFactory) model).setAmountToStepBy(tempInnerIncrement.doubleValue());
            }

            BigDecimal outerStepSize = new BigDecimal("1");

            BigDecimal tempOuterIncrement = ControlHelper.getOuterIncrementValue(control, getAtdl4jOptions());
            if (tempOuterIncrement != null) {
                outerStepSize = tempOuterIncrement;
            }

            spinner.valueProperty().addListener(new DoubleSpinnerListener(spinner, outerStepSize, outerStepSize.negate()));

        } else if (control instanceof SingleSpinnerT) {
            BigDecimal tempIncrement = ControlHelper.getIncrementValue(control, getAtdl4jOptions(), digits);
            if (tempIncrement != null) {
                // -- Handle initValue="2.5" and ensure that we don't wind up using BigDecimal unscaled and end up with "25" --
                ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setAmountToStepBy(tempIncrement.intValue());
            } else // tempIncrement is null
            {
                if (digits != 0) {
                    // -- Set the increment to the precision associated with digits (eg ".01" when digits=2, ".001" when digits=3) --
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setAmountToStepBy(new BigDecimal(Math.pow(10, -digits)).setScale(digits, RoundingMode.HALF_UP).intValue());
                } else {
                    ((SpinnerValueFactory.IntegerSpinnerValueFactory) model).setAmountToStepBy(new BigDecimal("1").intValue());
                }
            }
        }

        spinner.setPrefHeight(22);
        spinner.setPrefWidth(150);

        applyInitialValue();

        return components;
    }
}
