package com.ucl.hottopic.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 16-2-22
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
public class DatePrintable extends Date {
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public DatePrintable(Date date) {
        super(date.getTime());
    }

    @Override
    public String toString() {
        String ds = df.format(this);
        return ds.substring(0, 22) + ":" + ds.substring(22);
    }
}
