package com.ucl.hottopic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */

@Document(collection = "HotWord")
public class HotWord {
    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date time;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date end;
    private Word word;

    public String getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Word getWord() {
        return word;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}

class Word {
    private List<Integer> appear;
    private String text;
    private double freq;
    private double trend;
    private double total;
    //private Dictionary<String, Double> leftNeighbor;
    private boolean leftEnd;
    //private Dictionary<String, Double> rightNeighbor;
    private boolean rightEnd;
    private double leftEntropy;
    private double rightEntropy;
    private double mi;
    private boolean useless;

    public List<Integer> getAppear() {
        return appear;
    }

    public String getText() {
        return text;
    }

    public double getFreq() {
        return freq;
    }

    public double getTrend() {
        return trend;
    }

    public double getTotal() {
        return total;
    }

    public boolean isLeftEnd() {
        return leftEnd;
    }

    public boolean isRightEnd() {
        return rightEnd;
    }

    public double getLeftEntropy() {
        return leftEntropy;
    }

    public double getRightEntropy() {
        return rightEntropy;
    }

    public double getMi() {
        return mi;
    }

    public boolean isUseless() {
        return useless;
    }

    public void setAppear(List<Integer> appear) {
        this.appear = appear;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public void setTrend(double trend) {
        this.trend = trend;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setLeftEnd(boolean leftEnd) {
        this.leftEnd = leftEnd;
    }

    public void setRightEnd(boolean rightEnd) {
        this.rightEnd = rightEnd;
    }

    public void setLeftEntropy(double leftEntropy) {
        this.leftEntropy = leftEntropy;
    }

    public void setRightEntropy(double rightEntropy) {
        this.rightEntropy = rightEntropy;
    }

    public void setMi(double mi) {
        this.mi = mi;
    }

    public void setUseless(boolean useless) {
        this.useless = useless;
    }
}
