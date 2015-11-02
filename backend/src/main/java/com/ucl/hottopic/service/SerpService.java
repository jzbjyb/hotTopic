package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.BaiduSerp;
import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.repository.BaiduSerpRepository;
import com.ucl.hottopic.service.util.ArrayListPrintable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */

@Service
public class SerpService {
    @Autowired
    private BaiduSerpRepository baiduSerpRepository;

    public BaiduSerp getOneBaiduSerp(String id) {
        return baiduSerpRepository.findOne(id);
    }

    public List<String> getBaiduSerpIds(List<HotTopic> hotTopics) {
        List<String> ids = new ArrayList<String>();
        for(HotTopic ht : hotTopics) {
            if(ht.getBaiduSerpId() == null) continue;
            ids.add(ht.getBaiduSerpId());
        }
        return ids;
    }

    public List<BaiduSerp> getMultiBaiduSerps(List<String> ids) {
        return baiduSerpRepository.findByIds(new ArrayListPrintable<String>(ids));
    }
}
