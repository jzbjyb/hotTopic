package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.domain.HotTopicCluster;
import com.ucl.hottopic.repository.HotTopicClusterRepository;
import com.ucl.hottopic.repository.HotTopicRepository;
import com.ucl.hottopic.service.util.ArrayListPrintable;
import com.ucl.hottopic.service.util.DatePrintable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-17
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */

@Service
public class HotTopicService {
    private Logger logger = Logger.getLogger(HotTopicService.class);
    @Autowired
    private HotTopicRepository hotTopicRepository;
    @Autowired
    private HotTopicClusterRepository hotTopicClusterRepository;

    public HotTopicCluster getOneHotTopicCluster(String id) {
        return hotTopicClusterRepository.findOne(id);
    }

    @Cacheable(value = "hottopicCluster", key = "#start.toString() + '-' + " +
            "#end.toString()", unless = "#result == null")
    public HotTopicCluster getClusterByTime(Date start, Date end) {
        logger.info(String.format("update cluster cache at %s", DatatypeConverter.printDateTime(Calendar.getInstance())));
        return hotTopicClusterRepository.findByStartAndEnd(start, end);
    }

    @Cacheable(value = "hottopicCluster", key = "(#end.getYear() * 100000 + #end.getMonth() * 10000 + #end.getDate() * 100 + #end.getHours())" +
            " + '-' + #pageNum + '-' + #pageSize", unless = "#result.size() == 0")
    public List<HotTopicCluster> getClusterPage(Date end, int pageNum, int pageSize) {
        logger.info(String.format("update cluster list cache at %s", DatatypeConverter.printDateTime(Calendar.getInstance())));
        // sort according end then start
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(
                Arrays.asList(new Sort.Order(Sort.Direction.DESC, "end"))
        ));
        List<HotTopicCluster> cs = new ArrayList<HotTopicCluster>(hotTopicClusterRepository.findByEndBefore(new DatePrintable(end), request).getContent());
        Collections.sort(cs, new Comparator<HotTopicCluster>() {
            @Override
            public int compare(HotTopicCluster o1, HotTopicCluster o2) {
                // order by 1) end desc 2) start asc
                return (-o1.getEnd().compareTo(o2.getEnd()) * 10) + o1.getStart().compareTo(o2.getStart());
            }
        });
        return cs;
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
