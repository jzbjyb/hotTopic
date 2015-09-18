package com.ucl.hottopic.test.service.cluster;

import com.ucl.hottopic.Application;
import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.domain.TopicCluster;
import com.ucl.hottopic.service.HotTopicService;
import com.ucl.hottopic.service.cluster.Cluster;
import com.ucl.hottopic.service.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.LinkageError;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ClusterTest {
    @Autowired
    private HotTopicService hotTopicService;
    @Autowired
    private Cluster cluster;

    @Test
    public void testCluster() {
        List<HotTopic> hts = hotTopicService.getAll();
        List<TopicCluster> clusterList = cluster.cluster(hts, 0.6);
        for(TopicCluster c : clusterList) {
            List<String> titles = new ArrayList<String>(c.getCluster().keySet());
            String[] titleArray = new String[titles.size()];
            titles.toArray(titleArray);
            System.out.println(c.getTitle() + " " + Util.join(titleArray, "#"));
        }
    }
}