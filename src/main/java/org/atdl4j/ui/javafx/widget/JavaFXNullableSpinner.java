package org.atdl4j.ui.javafx.widget;

import java.math.BigDecimal;
import javafx.scene.control.Spinner;

/**
 *
 * @author daniel.makgonta
 */
public class JavaFXNullableSpinner extends Spinner {

    private static final long serialVersionUID = 2947835451995064559L;
    public static BigDecimal MIN_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal(-Integer.MAX_VALUE);
    public static BigDecimal MAX_INTEGER_VALUE_AS_BIG_DECIMAL = new BigDecimal(Integer.MAX_VALUE);
    
    public JavaFXNullableSpinner(){
        super();
    }
}
