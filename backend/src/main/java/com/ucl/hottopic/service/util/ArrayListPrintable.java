package com.ucl.hottopic.service.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-19
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class ArrayListPrintable<T>  extends ArrayList<T> {
    public ArrayListPrintable(List<T> list) {
        super(list);
    }

    @Override
    public String toString() {
        String[] sArr = new String[this.size()];
        for(int i=0; i<this.size(); i++) {
            sArr[i] = "'" + this.get(i).toString() + "'";
        }
        return "[" + Util.join(sArr, ",") + "]";
    }
}
