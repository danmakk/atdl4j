package org.atdl4j.ui.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.atdl4j.data.ValidationRule;

/**
 *
 * @author daniel.makgonta
 */
public abstract class JavaFXListener implements EventHandler<ActionEvent>, ChangeListener<Object> {

    protected abstract void handleEvent() ;
    
    protected abstract JavaFXWidget<?> getAffectedWidget();

    protected abstract ValidationRule getRule();

    protected abstract void setCxlReplaceMode(boolean cxlReplaceMode);

    protected abstract void handleLoadFixMessageEvent();

    @Override
    public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
        
    }
    
    @Override
    public void handle(ActionEvent event) {

    }
}
