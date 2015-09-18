package com.ucl.hottopic.domain;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-18
 * Time: 上午9:01
 * To change this template use File | Settings | File Templates.
 */
public class TopicCluster implements Comparable<TopicCluster> {
    private String title;
    private double score;
    private Map<String, Double> cluster = new HashMap<String, Double>();
    private List<HotTopic> items = new ArrayList<HotTopic>();

    private Logger logger = Logger.getLogger(TopicCluster.class);

    public void push(String title, List<HotTopic> hotTopics) {
        if(!cluster.containsKey(title)) cluster.put(title, 0.0);
        cluster.put(title, (double)hotTopics.size() + cluster.get(title));
        items.addAll(hotTopics);
        score += (double)hotTopics.size();
    }

    @Override
    public int compareTo(TopicCluster topicCluster) {
        return -((Double)score).compareTo(topicCluster.getScore());
    }

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public Map<String, Double> getCluster() {
        return cluster;
    }

    public List<HotTopic> getItems() {
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

    public void setItems(List<HotTopic> items) {
        this.items = items;
    }

    public TopicClusterExt toExternal() {
        TopicClusterExt topicClusterExt = new TopicClusterExt();
        List<String> ids = new ArrayList<String>();
        for(HotTopic ht : this.items) ids.add(ht.getId());
        topicClusterExt.setItems(ids);
        topicClusterExt.setCluster(this.cluster);
        topicClusterExt.setScore(this.score);
        topicClusterExt.setTitle(this.title);
        return topicClusterExt;
    }
}
