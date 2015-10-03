package com.ucl.hottopic.service.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-18
 * Time: 上午8:20
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static String join(String[] strAry, String join) {
        StringBuffer sb=new StringBuffer();
        for(int i=0; i<strAry.length; i++) {
            if(i == strAry.length-1) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }

    public static Date getDateTime(Date start, int offset) {
        Date newDate = (Date)start.clone();
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, offset);
        return c.getTime();
    }

    public static int getLCS(String s1, String s2) {
        if(s1 == null || s2 == null) return 0;
        int[][] trace = new int[s1.length() + 1][s2.length() + 1];
        char[] s1c = s1.toCharArray();
        char[] s2c = s2.toCharArray();
        for(int i=1; i<=s1c.length; i++) {
            for(int j=1; j<=s2c.length; j++) {
                trace[i][j] = trace[i-1][j-1];
                if(s1c[i-1] == s2c[j-1]) {
                    trace[i][j]++;
                }
                trace[i][j] = Math.max(trace[i][j], Math.max(trace[i-1][j], trace[i][j-1]));
            }
        }
        return trace[s1c.length][s2c.length];
    }

    public static double getSim(String s1, String s2, SimilarityType type) {
        if(s1 == null || s1.length() == 0 || s2 == null || s2.length() == 0) return 0;
        int lcs = getLCS(s1, s2);
        switch(type) {
            case MIN:
                return (double)lcs / (double)Math.min(s1.length(), s2.length());
            case MAX:
                return (double)lcs / (double)Math.max(s1.length(), s2.length());
        }
        return 0;
    }

    public static double getDis(String s1, String s2, SimilarityType type) {
        return 1 - getSim(s1, s2, type);
    }
}
