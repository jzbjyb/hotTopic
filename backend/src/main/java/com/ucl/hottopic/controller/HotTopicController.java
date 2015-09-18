package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.domain.RestApi;
import com.ucl.hottopic.domain.TopicCluster;
import com.ucl.hottopic.domain.TopicClusterExt;
import com.ucl.hottopic.service.HotTopicService;
import com.ucl.hottopic.service.cluster.Cluster;
import com.ucl.hottopic.service.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-17
 * Time: 下午11:25
 * To change this template use File | Settings | File Templates.
 */

@RestController
public class HotTopicController {
    private Logger logger = Logger.getLogger(HotTopicController.class);

    @Autowired
    private HotTopicService hotTopicService;
    @Autowired
    private Cluster cluster;

    @RequestMapping(value = "/hottopics", method = RequestMethod.GET)
    public RestApi getRange(@RequestParam(value="start", required = false) String start, @RequestParam(value = "end", required = false) String end) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat();
        List<HotTopic> hts;
        if(start == null && end == null) {
            hts = hotTopicService.getAll();
        } else {
            Date now = new Date();
            Date startDate = start != null ? DatatypeConverter.parseDateTime(start).getTime() : null;
            Date endDate = end != null ? DatatypeConverter.parseDateTime(end).getTime() : null;
            startDate = startDate != null && startDate.compareTo(now) > 0 ? (Date)now.clone() : startDate;
            endDate = endDate != null && endDate.compareTo(now) > 0 ? (Date)now.clone() : endDate;
            if(start == null) {
                startDate = Util.getDateTime(endDate, -1);
            }
            else if(end == null) {
                endDate = Util.getDateTime(startDate, 1);
                endDate = endDate.compareTo(now) > 0 ? (Date)now.clone() : endDate;
            }
            logger.info(dateFormat.format(startDate) + dateFormat.format(endDate));
            hts = hotTopicService.getBetween(startDate, endDate);
        }
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(hts));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET)
    public RestApi getCluster(@RequestParam(value="start", required = false) String start, @RequestParam(value = "end", required = false) String end, @RequestParam(value = "dia", defaultValue = "0.5") double diameter) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat();
        List<TopicCluster> clusters;
        List<HotTopic> hts;
        if(start == null && end == null) {
            hts = hotTopicService.getAll();
        } else {
            Date now = new Date();
            Date startDate = start != null ? DatatypeConverter.parseDateTime(start).getTime() : null;
            Date endDate = end != null ? DatatypeConverter.parseDateTime(end).getTime() : null;
            startDate = startDate != null && startDate.compareTo(now) > 0 ? (Date)now.clone() : startDate;
            endDate = endDate != null && endDate.compareTo(now) > 0 ? (Date)now.clone() : endDate;
            if(start == null) {
                startDate = Util.getDateTime(endDate, -1);
            }
            else if(end == null) {
                endDate = Util.getDateTime(startDate, 1);
                endDate = endDate.compareTo(now) > 0 ? (Date)now.clone() : endDate;
            }
            logger.info(dateFormat.format(startDate) + dateFormat.format(endDate));
            hts = hotTopicService.getBetween(startDate, endDate);
        }
        clusters = cluster.cluster(hts, diameter);
        List<TopicClusterExt> topicClusterExts = new ArrayList<TopicClusterExt>();
        for(TopicCluster tc : clusters) topicClusterExts.add(tc.toExternal());
        return new RestApi().setData(RestApi.getDataInstance().setTopicClusterExts(topicClusterExts));
    }

    @RequestMapping(value = "/hottopics", method = RequestMethod.POST)
    public RestApi post(@RequestBody HotTopic hotTopic) {
        HotTopic newHotTopic = hotTopicService.create(hotTopic);
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(newHotTopic));
    }
}
