package com.ucl.hottopic.editor;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ucl.hottopic.service.util.Util;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-5
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class DoubleSerializer extends JsonSerializer<Double> {
    private static final String pattern = "#.####";

    public String format(Double d, int place) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        double r = Util.round(d, place);
        return myFormatter.format(r);
    }

    @Override
     public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (value == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(format(value, 5));
        }
    }
}
