package com.ucl.hottopic.service.cluster;

import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.domain.TopicCluster;
import com.ucl.hottopic.service.util.SimilarityType;
import com.ucl.hottopic.service.util.Util;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-18
 * Time: 上午9:49
 * To change this template use File | Settings | File Templates.
 */

@Service
public class Cluster {
    private Logger logger = Logger.getLogger(Cluster.class);

    public List<TopicCluster> cluster(List<HotTopic> hotTopics, double diameter) {
        Map<String, List<HotTopic>> titleMap = new HashMap<String, List<HotTopic>>();
        for(HotTopic ht : hotTopics) {
            if(ht.getTitle() == null) continue;
            if(!titleMap.containsKey(ht.getTitle())) {
                titleMap.put(ht.getTitle(), new ArrayList<HotTopic>());
            }
            titleMap.get(ht.getTitle()).add(ht);
        }
        List<Map.Entry<String, List<HotTopic>>> titleList = new ArrayList<Map.Entry<String, List<HotTopic>>>(titleMap.entrySet());
        Collections.sort(titleList, new Comparator<Map.Entry<String, List<HotTopic>>>() {
            public int compare(Map.Entry<String, List<HotTopic>> arg0, Map.Entry<String, List<HotTopic>> arg1) {
                return - ((Integer)arg0.getValue().size()).compareTo(arg1.getValue().size());
            }
        });
        List<TopicCluster> clusters = new ArrayList<TopicCluster>();
        TopicCluster curCluster = null;
        while(titleList.size() > 0) {
            if(curCluster == null) {
                Map.Entry<String, List<HotTopic>> curTitle = titleList.get(0);
                curCluster = new TopicCluster();
                curCluster.push(curTitle.getKey(), curTitle.getValue());
                curCluster.setTitle(curTitle.getKey());
                titleList.remove(0);
            } else {
                double minDis = Double.MAX_VALUE;
                Map.Entry<String, List<HotTopic>> minTitle = null;
                for(Map.Entry<String, List<HotTopic>> t : titleList) {
                    double dis = getDis(t.getKey(), new ArrayList<String>(curCluster.getCluster().keySet()));
                    if(dis < minDis) {
                        minDis = dis;
                        minTitle = t;
                    }
                }
                if(minTitle == null || minDis >= diameter) {
                    // one cluster complete
                    clusters.add(curCluster);
                    curCluster = null;
                }  else {
                    // continue expand cluster
                    curCluster.push(minTitle.getKey(), minTitle.getValue());
                    titleList.remove(titleList.indexOf(minTitle));
                }
            }
        }
        Collections.sort(clusters);
        return clusters;
    }

    private double getDis(String title, List<String> cluster) {
        double dis = Double.MAX_VALUE;
        for(String t : cluster) {
            double thisDis = Util.getDis(title, t, SimilarityType.MIN);
            if(thisDis < dis) dis = thisDis;
        }
        return dis > 1 ? 1 : dis;
    }
}
