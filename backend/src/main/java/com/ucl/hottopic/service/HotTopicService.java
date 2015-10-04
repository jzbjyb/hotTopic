package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.domain.HotTopicCluster;
import com.ucl.hottopic.repository.HotTopicClusterRepository;
import com.ucl.hottopic.repository.HotTopicRepository;
import com.ucl.hottopic.service.util.ArrayListPrintable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-17
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */

@Service
public class HotTopicService {
    @Autowired
    private HotTopicRepository hotTopicRepository;
    @Autowired
    private HotTopicClusterRepository hotTopicClusterRepository;

    public HotTopicCluster getOneHotTopicCluster(String id) {
        return hotTopicClusterRepository.findOne(id);
    }

    public HotTopicCluster getClusterByTime(Date start, Date end) {
        return hotTopicClusterRepository.findByStartAndEnd(start, end);
    }

    public Page<HotTopicCluster> getClusterPage(Date end, int pageNum, int pageSize) {
        // sort according end then start
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(Sort.Direction.DESC, new ArrayList<String>(Arrays.asList("end", "start"))));
        return hotTopicClusterRepository.findByEndLessThanEqual(end, request);
    }

    public List<HotTopicCluster> getAllClusters() {
        return (List<HotTopicCluster>)hotTopicClusterRepository.findAll();
    }

    public List<HotTopic> getHotTopicBetween(Date start, Date end) {
        return hotTopicRepository.findByTimeBetween(start, end);
    }

    public Page<HotTopic>  getHotTopicBetween(Date start, Date end, int pageNum, int pageSize) {
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(Sort.Direction.DESC, "time"));
        return hotTopicRepository.findByTimeBetween(start, end, request);
    }

    public HotTopic getOneHotTopic(String id) {
        return hotTopicRepository.findOne(id);
    }

    public List<HotTopic> getMultiHotTopic(List<String> ids) {
        return hotTopicRepository.findByIds(new ArrayListPrintable<String>(ids));
    }

    public Page<HotTopic> getLatestHotTopics(int pageNum, int pageSize) {
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(Sort.Direction.DESC, "time"));
        return hotTopicRepository.findAll(request);
    }

    public List<HotTopic> getAllHotTopic() {
        return (List<HotTopic>)hotTopicRepository.findAll();
    }

    public HotTopic createHotTopic(HotTopic hotTopic) {
        return hotTopicRepository.save(hotTopic);
    }
}
