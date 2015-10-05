package com.ucl.hottopic.controller;

import com.ucl.hottopic.editor.DoubleEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-5
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */

@ControllerAdvice
public class CustomBinder {
    @InitBinder
    public void initDoubleBinder(WebDataBinder binder) throws Exception {
        final String pattern = ".###";
        final DecimalFormat df = new DecimalFormat(pattern);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setDecimalFormatSymbols(dfs);
        binder.registerCustomEditor(double.class, new DoubleEditor(df));
    }
}
