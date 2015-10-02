package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.*;
import com.ucl.hottopic.service.HotTopicService;
import com.ucl.hottopic.service.cluster.Cluster;
import com.ucl.hottopic.service.util.Util;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private static int MAX_PAGESIZE = 10;

    @Autowired
    private HotTopicService hotTopicService;
    @Autowired
    private Cluster cluster;

    @RequestMapping(value = "/hottopics", method = RequestMethod.GET, params = {"ids"})
    public RestApi getHotTopicsById(@RequestParam(value = "ids") String ids) {
        String[] idArr = ids.split(",");
        List<HotTopic> hts = hotTopicService.getMultiHotTopic(new ArrayList<String>(Arrays.asList(idArr)));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(hts));
    }

    @RequestMapping(value = "/hottopics", method = RequestMethod.GET, params = {"start", "end", "pageNum", "pageSize"})
    public RestApi getHotTopicsByTime(@RequestParam(value = "start") String start, @RequestParam(value = "end") String end,
                                  @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "1")int pageSize) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGESIZE) pageSize = MAX_PAGESIZE;
        List<HotTopic> hts = new ArrayList<HotTopic>();
        Date now = new Date();
        if(start.equals("") && end.equals("")) {
            return getHotTopicsByTime(Util.ISO_FORMAT.format(Util.getDateTime(now, -1)), Util.ISO_FORMAT.format(now), pageNum, pageSize);
        } else {
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
            hts = hotTopicService.getHotTopicBetween(startDate, endDate, pageNum, pageSize).getContent();
        }
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(hts));
    }

    @RequestMapping(value = "/hottopics", method = RequestMethod.GET)
    public RestApi getHotTopics() {
        Date now = new Date();
        return getHotTopicsByTime(Util.ISO_FORMAT.format(Util.getDateTime(now, -1)), Util.ISO_FORMAT.format(now), 1, 1);
    }

    @RequestMapping(value = "/hottopics/{id}", method = RequestMethod.GET)
    public RestApi getHotTopicById(@PathVariable String id) {
        HotTopic ht = hotTopicService.getOneHotTopic(id);
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(ht));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET, params = {"date", "scope"})
    public RestApi getClustersByTime(@RequestParam(value = "date") String date, @RequestParam(value = "scope", defaultValue = "24") int scope) {
        Calendar now = Calendar.getInstance();
        if(!date.equals("")) {
            now.setTime(DatatypeConverter.parseDateTime(date).getTime());
        }
        Calendar end = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR), 0, 0);
        Set<Integer> scopeAllowed = new HashSet<Integer>(Arrays.asList(24, 2));
        if(!scopeAllowed.contains(scope)) {
            int minDis = Integer.MIN_VALUE;
            int minScope = 0;
            for(int al : scopeAllowed) {
                if(Math.abs(al - scope) < minDis) {
                    minDis = Math.abs(al - scope);
                    minScope = al;
                }
            }
            scope = minScope;
        }
        Calendar start = Calendar.getInstance();
        start.setTime(end.getTime());
        start.add(Calendar.HOUR_OF_DAY, -scope);
        HotTopicCluster htc = hotTopicService.getClusterByTime(start.getTime(), end.getTime());
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(htc));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET, params = {"before", "pageNum", "pageSize"})
    public RestApi getClusterList(@RequestParam(value = "before") String date, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGESIZE) pageSize = MAX_PAGESIZE;
        Date d = new Date();
        if(!date.equals("")) {
            d = DatatypeConverter.parseDateTime(date).getTime();
        }
        List<HotTopicCluster> clusters = hotTopicService.getClusterPage(d, pageNum, pageSize).getContent();
        List<HotTopicCluster> clustersExt = new ArrayList<HotTopicCluster>();
        for(HotTopicCluster htc : clusters) {
            clustersExt.add(htc.toExternal(20));
        }
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(clustersExt));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET)
    public RestApi getCluster() {
        return getClusterList(Util.ISO_FORMAT.format(new Date()), 1, 1);
    }

    @RequestMapping(value = "/clusters/{id}", method = RequestMethod.GET)
    public RestApi getClusterById(@PathVariable String id) {
        HotTopicCluster htc = hotTopicService.getOneHotTopicCluster(id);
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(htc));
    }
}
