package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.HotWordCluster;
import com.ucl.hottopic.repository.HotWordClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午11:18
 * To change this template use File | Settings | File Templates.
 */

@Service
public class HotWordClusterService {
    @Autowired
    private HotWordClusterRepository hotWordClusterRepository;

    public HotWordCluster getOneHotTopicCluster(String id) {
        return hotWordClusterRepository.findOne(id);
    }

    public HotWordCluster getClusterByTime(Date start, Date end) {
        return hotWordClusterRepository.findByStartAndEnd(start, end);
    }

    public Page<HotWordCluster> getClusterPage(Date end, int pageNum, int pageSize) {
        // sort according end then start
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(
                Arrays.asList(new Sort.Order(Sort.Direction.DESC, "end"), new Sort.Order(Sort.Direction.ASC, "start"))
        ));
        return hotWordClusterRepository.findByEndLessThanEqual(end, request);
    }
}
