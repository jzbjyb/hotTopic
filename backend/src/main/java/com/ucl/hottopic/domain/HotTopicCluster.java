package com.ucl.hottopic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date end;
    private List<OneCluster> clusters;

    public List<String> getHotTopicIds(String title) {
        for(OneCluster oc : clusters) {
            if(oc.getTitle().equals(title)) {
                return oc.getItems();
            }
        }
        return new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<OneCluster> getClusters() {
        return clusters;
    }

    public void setId(String id) {
        this.id = id;
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

    public HotTopicCluster toExternal(int sc, int ec, int maxLen) {
        HotTopicCluster htc = new HotTopicCluster();
        htc.setId(this.id);
        htc.setEnd(this.end);
        htc.setStart(this.start);
        List<OneCluster> clusters = new ArrayList<OneCluster>();
        if(this.clusters != null) {
            for(OneCluster oc : this.clusters.subList(Math.min(sc, this.clusters.size()), Math.min(ec, this.clusters.size()))) {
                OneCluster cp = new OneCluster();
                cp.setAlias(oc.getAlias().subList(0, Math.min(oc.getAlias().size(), maxLen)));
                cp.setKeywords(oc.getKeywords().subList(0, Math.min(oc.getKeywords().size(), maxLen)));
                cp.setEntities(oc.getEntities().subList(0, Math.min(oc.getEntities().size(), maxLen)));
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
    private String title;
    private double score;
    private List<OneName> alias;
    private List<OneName> keywords;
    private List<OneName> entities;
    private List<String> items;

    public List<String> getItems() {
        return items;
    }

    public List<OneName> getAlias() {
        return alias;
    }

    public List<OneName> getKeywords() {
        return keywords;
    }

    public List<OneName> getEntities() {
        return entities;
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

    public void setKeywords(List<OneName> keywords) {
        this.keywords = keywords;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setEntities(List<OneName> entities) {
        this.entities = entities;
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
