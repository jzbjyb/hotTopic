package com.ucl.hottopic.editor;

import org.springframework.beans.propertyeditors.PropertiesEditor;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-5
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */

public class DoubleEditor extends PropertiesEditor {
    private DecimalFormat doubleFrmatter;

    public DoubleEditor() {}

    public DoubleEditor(DecimalFormat doubleFrmatter) {
        this.doubleFrmatter = doubleFrmatter;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.equals("")) {
            text = "0";
        }
        setValue(Double.parseDouble(text));
    }

    @Override
    public String getAsText() {
        if(doubleFrmatter != null) return doubleFrmatter.format(getValue());
        return getValue().toString();
    }
}