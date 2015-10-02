package com.ucl.hottopic.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-1
 * Time: 下午8:28
 * To change this template use File | Settings | File Templates.
 */

@Document(collection = "HotTopicCluster")
public class HotTopicCluster {
    private Date start;
    private Date end;
    private List<OneCluster> clusters;

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<OneCluster> getClusters() {
        return clusters;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setClusters(List<OneCluster> clusters) {
        this.clusters = clusters;
    }

    public HotTopicCluster toExternal(int maxLen) {
        HotTopicCluster htc = new HotTopicCluster();
        htc.setEnd(this.end);
        htc.setStart(this.start);
        List<OneCluster> clusters = new ArrayList<OneCluster>();
        if(this.clusters != null) {
            for(OneCluster oc : this.clusters) {
                OneCluster cp = new OneCluster();
                cp.setAlias(oc.getAlias().subList(0, Math.min(oc.getAlias().size(), maxLen)));
                cp.setKeyword(oc.getKeyword().subList(0, Math.min(oc.getKeyword().size(), maxLen)));
                cp.setScore(oc.getScore());
                cp.setTitle(oc.getTitle());
                cp.setItems(new ArrayList<String>());
                clusters.add(cp);
            }
        }
        htc.setClusters(clusters);
        return htc;
    }
}

class OneCluster {
    private List<String> items;
    private List<OneName> alias;
    private List<OneName> keyword;
    private String title;
    private double score;

    public List<String> getItems() {
        return items;
    }

    public List<OneName> getAlias() {
        return alias;
    }

    public List<OneName> getKeyword() {
        return keyword;
    }

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setAlias(List<OneName> alias) {
        this.alias = alias;
    }

    public void setKeyword(List<OneName> keyword) {
        this.keyword = keyword;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

class OneName {
    private String title;
    private double score;

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
