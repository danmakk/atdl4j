package org.atdl4j.ui.javafx.widget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.StringConverter;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;
import org.atdl4j.data.Atdl4jConstants;
import org.atdl4j.data.converter.DateTimeConverter;
import org.atdl4j.fixatdl.core.LocalMktDateT;
import org.atdl4j.fixatdl.core.MonthYearT;
import org.atdl4j.fixatdl.core.TZTimeOnlyT;
import org.atdl4j.fixatdl.core.TZTimestampT;
import org.atdl4j.fixatdl.core.UTCDateOnlyT;
import org.atdl4j.fixatdl.core.UTCTimeOnlyT;
import org.atdl4j.fixatdl.core.UTCTimestampT;
import org.atdl4j.fixatdl.core.UseT;
import org.atdl4j.fixatdl.layout.ClockT;
import org.atdl4j.ui.impl.ControlHelper;
import org.atdl4j.ui.javafx.JavaFXListener;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXJideClockWidget extends AbstractJavaFXWidget<Comparable<DateTime>> {

    private static final Logger logger = Logger.getLogger(JavaFXJideClockWidget.class);
    public static boolean showEnabledButton = false;
    public static boolean show24HourClock = true;
    private boolean valueFilledIn;
    private boolean enabled = true;
    public boolean hasLabelOrCheckbox = false;
    private CheckBox enabledButton;
    private Label label;
    private DatePicker dateClock;
    private TextField timeClock;
    private boolean showMonthYear;
    private boolean showDay;
    private boolean showTime;
    private boolean useNowAsDate = false;

    public DateTimeZone getLocalMktTz()
            throws IllegalArgumentException {
        return DateTimeConverter.convertTimezoneToDateTimeZone(((ClockT) control).getLocalMktTz());
    }

    @Override
    protected void processReinit(Object aControlInitValue) {
        if (aControlInitValue != null) {
            // -- apply initValue if one has been specified --
            setAndRenderInitValue((XMLGregorianCalendar) aControlInitValue, ((ClockT) control).getInitValueMode());
        } else {
            // -- reinit the time to present time --
            setValue(new DateTime());
            valueFilledIn = (enabledButton != null); // the editor requires a value but until the 
            // enabledButton is checked, considere that no value is filled 
            updateFromModel();
        }
    }

    @Override
    protected void processNullValueIndicatorChange(Boolean aOldNullValueInd, Boolean aNewNullValueInd) {
        //do nothing
    }

    @Override
    public Comparable<DateTime> getControlValueRaw() {
        if ((dateClock == null) && (timeClock == null)) {
            return null; // disabled, no value to use
        }

        if (!valueFilledIn) {
            return null;
        }

        DateTime now = null;
        DateTime date = null;
        DateTime time = null;

        if (useNowAsDate) {
            now = new DateTime(DateTimeZone.getDefault());
        }
        if (showMonthYear) {
            date = new DateTime(dateClock.getValue());
        }
        if (showTime) {
            String[] parts = timeClock.getText().split(" ");
            if (parts.length > 3) {
                time = new DateTime(parts[2]);
            }
        }

        DateTime result;

        if (time != null) {
            result = new DateTime(useNowAsDate ? now.getYear() : showMonthYear ? date.getYear() : 1970,
                    useNowAsDate ? now.getMonthOfYear() : showMonthYear ? date.getMonthOfYear() : 1,
                    useNowAsDate ? now.getDayOfMonth() : showDay ? date.getDayOfMonth() : 1,
                    showTime ? time.getHourOfDay() : 0,
                    showTime ? time.getMinuteOfHour() : 0,
                    showTime ? time.getSecondOfMinute() : 0,
                    0,
                    DateTimeZone.getDefault());
        } else {
            result = new DateTime(useNowAsDate ? now.getYear() : showMonthYear ? date.getYear() : 1970,
                    useNowAsDate ? now.getMonthOfYear() : showMonthYear ? date.getMonthOfYear() : 1,
                    useNowAsDate ? now.getDayOfMonth() : showDay ? date.getDayOfMonth() : 1,
                    showTime ? now.getHourOfDay() : 0,
                    showTime ? now.getMinuteOfHour() : 0,
                    showTime ? now.getSecondOfMinute() : 0,
                    0,
                    DateTimeZone.getDefault());
        }

        // Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
        // Performing UTCDateT and MonthYearT coversion could produce an unexpected result.
        // No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
        if (parameter == null || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT) {
            result = result.withZone(DateTimeZone.UTC);
            logger.debug("getControlValue() parameter: " + parameter + " result: " + result);
        }
        return result;
    }

    @Override
    public void setValue(Comparable<DateTime> value) {
        // Convert to UTC time for UTCTimestampT and UTCTimeOnlyT.
        // Performing UTCDateT and MonthYearT coversion could produce an unexpected result.
        // No conversion is needed for LocalMktTimeT, TZTimestampT, and TZTimeOnlyT.
        if (parameter == null || parameter instanceof UTCTimestampT || parameter instanceof UTCTimeOnlyT) {
            logger.debug("setValue() parameter: " + parameter + " value: " + value);
            // -- no need to adjust DateTime --
        }

        LocalDate localDate = LocalDate.now();

        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();

        String[] parts = currentTime.toString().split(" ");

        if (showMonthYear) {
            dateClock.setValue(localDate);
        }
        if (showTime) {
            String hour;
            String minute;
            String second;

            int hr = ((DateTime) value).getHourOfDay();
            hour = String.valueOf(hr);
            if (hr < 10) {
                hour = "0".concat(hour);
            }

            int min = ((DateTime) value).getMinuteOfHour();
            minute = String.valueOf(min);
            if (min < 10) {
                minute = "0".concat(minute);
            }

            int sec = ((DateTime) value).getSecondOfMinute();
            second = String.valueOf(sec);
            if (sec < 10) {
                second = "0".concat(second);
            }

            timeClock.setText(hour + ":" + minute + ":" + second);

        }
        valueFilledIn = true;
        updateFromModel();
    }

    @Override
    public void addListener(JavaFXListener listener) {
        if (showMonthYear) {
            dateClock.valueProperty().addListener(listener);
            dateClock.setOnAction(listener);
        }
        if (showTime) {
            timeClock.setOnAction(listener);
            timeClock.textProperty().addListener(listener);
        }

        if (enabledButton != null) {
            enabledButton.selectedProperty().addListener(listener);
        }
    }

    @Override
    public void removeListener(JavaFXListener listener) {
        if (showMonthYear) {
            dateClock.valueProperty().removeListener(listener);
            dateClock.setOnAction(null);
        }
        if (showTime) {
            timeClock.textProperty().removeListener(listener);
            timeClock.setOnAction(null);
        }

        if (enabledButton != null) {
            enabledButton.selectedProperty().removeListener(listener);
        }
    }

    protected void setAndRenderInitValue(XMLGregorianCalendar aValue, int aInitValueMode) {
        if (aValue != null) {
            // -- Note that this will throw IllegalArgumentException if timezone ID
            // specified cannot be resolved --
            DateTimeZone tempLocalMktTz = getLocalMktTz();
            logger.debug("control.getID(): " + control.getID() + " aValue: " + aValue + " getLocalMktTz(): " + tempLocalMktTz);

            // -- localMktTz is required when using/interpreting aValue --
            if (tempLocalMktTz == null) {
                throw new IllegalArgumentException("localMktTz is required when aValue (" + aValue + ") is specified. (Control.ID: "
                        + control.getID() + ")");
            }

            DateTime tempNow = new DateTime(tempLocalMktTz);

            DateTime tempInit = new DateTime(
                    (showMonthYear && aValue.getYear() != DatatypeConstants.FIELD_UNDEFINED) ? aValue.getYear() : tempNow.getYear(),
                    (showMonthYear && aValue.getMonth() != DatatypeConstants.FIELD_UNDEFINED) ? aValue.getMonth() : tempNow.getMonthOfYear(),
                    (showMonthYear && aValue.getDay() != DatatypeConstants.FIELD_UNDEFINED) ? aValue.getDay() : tempNow.getDayOfMonth(),
                    (showTime && aValue.getHour() != DatatypeConstants.FIELD_UNDEFINED) ? aValue.getHour() : 0,
                    (showTime && aValue.getMinute() != DatatypeConstants.FIELD_UNDEFINED) ? aValue.getMinute() : 0,
                    (showTime && aValue.getSecond() != DatatypeConstants.FIELD_UNDEFINED) ? aValue.getSecond() : 0,
                    0,
                    tempLocalMktTz);

            if ((aInitValueMode == Atdl4jConstants.CLOCK_INIT_VALUE_MODE_USE_CURRENT_TIME_IF_LATER)
                    && (tempNow.isAfter(tempInit))) {
                // -- Use current time --
                tempInit = tempNow;
            }

            // -- Make sure that the value is rendered on the display in local timezone --
            setValue(tempInit.withZone(DateTimeZone.getDefault()));
        }
    }

    @Override
    public void setFIXValue(String aFIXValue) {
        super.setFIXValue(aFIXValue);

        DateTime tempFIXValueTime = (DateTime) getControlValueRaw();
        DateTime tempCurrentTime = new DateTime();

        // -- Check to see if the time set is < current time --
        if (tempCurrentTime.isAfter(tempFIXValueTime)) {
            logger.debug("setFIXValue(" + aFIXValue + ") resulted in time < present (" + tempFIXValueTime + " < " + tempCurrentTime + ")");

            Integer tempClockPastTimeSetFIXValueRule = getAtdl4jOptions().getClockPastTimeSetFIXValueRule(getControl());
            logger.debug("Control: " + getControl().getID() + " tempClockPastTimeSetFIXValueRule: " + tempClockPastTimeSetFIXValueRule);

            if (getAtdl4jOptions().CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS.equals(tempClockPastTimeSetFIXValueRule)) {
                // -- keep as-is --
                logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_USE_AS_IS rule -- Retaining: " + tempFIXValueTime);
            } else if (getAtdl4jOptions().CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT.equals(tempClockPastTimeSetFIXValueRule)) {
                logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_CURRENT rule -- Setting: " + tempCurrentTime + " ( vs. " + tempFIXValueTime + ")");
                setValue(tempCurrentTime);
            } else if (getAtdl4jOptions().CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL.equals(tempClockPastTimeSetFIXValueRule)) {
                logger.debug("Per Atdl4jConfig.CLOCK_PAST_TIME_SET_FIX_VALUE_RULE_SET_TO_NULL rule -- Setting control to 'null value' ( vs. " + tempFIXValueTime + ")");
                setValueAsString(Atdl4jConstants.VALUE_NULL_INDICATOR);
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    protected List< ? extends Node> createBrickComponents() {

        List<Node> components = new ArrayList<Node>();

        // tooltip
        String tooltip = control.getTooltip();

        if (parameter instanceof UTCTimestampT || parameter instanceof TZTimestampT) {
            if (getAtdl4jOptions() == null || getAtdl4jOptions().isShowDateInputOnTimestampClockControl()) {
                showMonthYear = true;
                showDay = true;
            } else {
                showMonthYear = false;
                showDay = false;
                useNowAsDate = true;
            }
            showTime = true;
        } else if (parameter instanceof UTCDateOnlyT || parameter instanceof LocalMktDateT) {
            showMonthYear = true;
            showDay = true;
            showTime = false;
        } else if (parameter instanceof MonthYearT) {
            showMonthYear = true;
            showDay = false;
            showTime = false;
        } else if (parameter == null || parameter instanceof UTCTimeOnlyT || parameter instanceof TZTimeOnlyT) {
            showMonthYear = false;
            showDay = false;
            showTime = true;
        }

        if (getAtdl4jOptions() != null
                && getAtdl4jOptions().isShowEnabledCheckboxOnOptionalClockControl()
                && parameter != null
                && UseT.OPTIONAL.equals(parameter.getUse())) {
            hasLabelOrCheckbox = true;
            enabledButton = new CheckBox();

            if (control.getLabel() != null) {
                enabledButton.setText(control.getLabel());
            }
            
            Tooltip tip = new Tooltip("Click to enable optional parameter");
            enabledButton.setTooltip(tip);
            enabledButton.setSelected(false);

            enabledButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    updateFromView();
                }
            });

            enabledButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    updateFromView();
                }
            });

            components.add(enabledButton);

        } else if (control.getLabel() != null) {
            // add label
            hasLabelOrCheckbox = true;
            label = new Label();
            label.setPadding(new Insets(0, 5, 0, 0));
            label.setText(control.getLabel());
            if (tooltip != null) {
                Tooltip tip = new Tooltip(tooltip);
                label.setTooltip(tip);
            }
            components.add(label);
        }

        // date clock
        if (showMonthYear) {

            final String pattern = showDay ? "dd.MM.yyyy" : "MM.yyyy";

            dateClock = new DatePicker();
            dateClock.setPadding(new Insets(0, 5, 5, 5));
            dateClock.setPromptText((pattern).toLowerCase());
            dateClock.setConverter(new StringConverter<LocalDate>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });
            if (tooltip != null) {
                Tooltip tip = new Tooltip(tooltip);
                dateClock.setTooltip(tip);
            }
            components.add(dateClock);
        }

        // time clock
        if (showTime) {
            final String pattern = show24HourClock ? "HH:mm:ss" : "hh:mm:ss";

            timeClock = new JavaFXTimeTextFieldWidget();
            timeClock.setPadding(new Insets(0, 5, 5, 5));

            if (tooltip != null) {
                Tooltip tip = new Tooltip(tooltip);
                timeClock.setTooltip(tip);
            }
            components.add(timeClock);
        }

        // init value, if applicable
        setAndRenderInitValue((XMLGregorianCalendar) ControlHelper.getInitValue(control, getAtdl4jOptions()), ((ClockT) control).getInitValueMode());

        updateFromModel();
        return components;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        updateFromModel();
    }

    @Override
    public boolean isNullValue() {
        if (!valueFilledIn) {
            return true;
        } else {
            return super.isNullValue();
        }
    }

    private void updateFromView() {
        if (enabledButton != null) {
            valueFilledIn = enabledButton.isSelected();
        } else {
            valueFilledIn = true;
        }
        if ((timeClock != null) && (timeClock.isVisible())) {
            timeClock.setDisable(!(valueFilledIn && enabled));
        }
        if ((dateClock != null) && (dateClock.isVisible())) {
            dateClock.setDisable(!(valueFilledIn && enabled));
        }
    }

    private void updateFromModel() {
        if (enabledButton != null) {
            enabledButton.setSelected(valueFilledIn);
            enabledButton.setDisable(!enabled);
        }
        if ((timeClock != null) && (timeClock.isVisible())) {
            timeClock.setDisable(!(valueFilledIn && enabled));
        }
        if ((dateClock != null) && (dateClock.isVisible())) {
            dateClock.setDisable(!(valueFilledIn && enabled));
        }
    }

    @Override
    public List<Node> getComponents() {
        List<Node> widgets = new ArrayList<Node>();
        if (enabledButton != null) {
            widgets.add(enabledButton);
        }
        if (label != null) {
            widgets.add(label);
        }
        if (showMonthYear) {
            widgets.add(dateClock);
        }
        if (showTime) {
            widgets.add(timeClock);
        }
        return widgets;
    }

    @Override
    public List<Node> getComponentsExcludingLabel() {
        List<Node> widgets = new ArrayList<Node>();
        if (showMonthYear) {
            widgets.add(dateClock);
        }
        if (showTime) {
            widgets.add(timeClock);
        }
        return widgets;
    }
}
