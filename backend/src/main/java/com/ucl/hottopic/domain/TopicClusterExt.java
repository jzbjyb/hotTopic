package com.ucl.hottopic.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-18
 * Time: 下午7:10
 * To change this template use File | Settings | File Templates.
 */
public class TopicClusterExt {
    private String title;
    private double score;
    private Map<String, Double> cluster = new HashMap<String, Double>();
    private List<String> items = new ArrayList<String>();

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public Map<String, Double> getCluster() {
        return cluster;
    }

    public List<String> getItems() {
        return items;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setCluster(Map<String, Double> cluster) {
        this.cluster = cluster;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
