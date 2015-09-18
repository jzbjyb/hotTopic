package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.repository.HotTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    public List<HotTopic> getBetween(Date start, Date end) {
        return hotTopicRepository.findByTimeBetween(start, end);
    }

    public List<HotTopic> getAll() {
        return (List<HotTopic>)hotTopicRepository.findAll();
    }

    public HotTopic create(HotTopic hotTopic) {
        return hotTopicRepository.save(hotTopic);
    }
}
