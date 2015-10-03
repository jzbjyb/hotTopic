package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.*;
import com.ucl.hottopic.exception.Exception404;
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
    private static Set<Integer> scopeAllowed = new HashSet<Integer>(Arrays.asList(24, 2));

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
            // get current day's hot topics
            Calendar cal = Calendar.getInstance();
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(cal.getTime());
            startCal.add(Calendar.DAY_OF_MONTH, -1);
            return getHotTopicsByTime(DatatypeConverter.printDateTime(startCal), DatatypeConverter.printDateTime(cal), pageNum, pageSize);
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
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(now.getTime());
        start.add(Calendar.DAY_OF_MONTH, -1);
        return getHotTopicsByTime(DatatypeConverter.printDateTime(start), DatatypeConverter.printDateTime(now), 1, 1);
    }

    @RequestMapping(value = "/hottopics/{id}", method = RequestMethod.GET)
    public RestApi getHotTopicById(@PathVariable String id) {
        HotTopic ht = hotTopicService.getOneHotTopic(id);
        if(ht == null) throw new Exception404(String.format("Hot topic %s not found", id));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(ht));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET, params = {"date", "scope"})
    public RestApi getClustersByTime(@RequestParam(value = "date") String date, @RequestParam(value = "scope", defaultValue = "24") int scope) {
        Calendar now = Calendar.getInstance();
        if(!date.equals("")) {
            now.setTime(DatatypeConverter.parseDateTime(date).getTime());
        }
        Calendar end = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), 0, 0);

        if(!scopeAllowed.contains(scope)) {
            int minDis = Integer.MIN_VALUE;
            for(int al : scopeAllowed) {
                if(Math.abs(al - scope) < minDis) {
                    minDis = Math.abs(al - scope);
                    scope = al;
                }
            }
        }
        Calendar start = Calendar.getInstance();
        start.setTime(end.getTime());
        start.add(Calendar.HOUR_OF_DAY, -scope);
        DatatypeConverter.printDateTime(start);
        logger.info(String.format("GET CLUSTER %s - %s", DatatypeConverter.printDateTime(start), DatatypeConverter.printDateTime(start)));
        HotTopicCluster htc = hotTopicService.getClusterByTime(start.getTime(), end.getTime());
        if(htc == null) throw new Exception404(String.format("No cluster has been found with start %s and end %s",
                DatatypeConverter.printDateTime(start), DatatypeConverter.printDateTime(end)));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(htc.toExternal(20)));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET, params = {"before", "pageNum", "pageSize"})
    public RestApi getClusterList(@RequestParam(value = "before") String date, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGESIZE) pageSize = MAX_PAGESIZE;
        Calendar cal = Calendar.getInstance();
        if(!date.equals("")) {
            cal = DatatypeConverter.parseDateTime(date);
        }
        List<HotTopicCluster> clusters = hotTopicService.getClusterPage(cal.getTime(), pageNum, pageSize).getContent();
        List<HotTopicCluster> clustersExt = new ArrayList<HotTopicCluster>();
        for(HotTopicCluster htc : clusters) {
            clustersExt.add(htc.toExternal(20));
        }
        logger.info(String.format("GET CLUSTER before %s page %d with page size %d", DatatypeConverter.printDateTime(cal), pageNum, pageSize));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(clustersExt));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET)
    public RestApi getCluster() {
        return getClusterList(DatatypeConverter.printDateTime(Calendar.getInstance()), 1, 1);
    }

    @RequestMapping(value = "/clusters/{id}", method = RequestMethod.GET)
    public RestApi getClusterById(@PathVariable String id) {
        HotTopicCluster htc = hotTopicService.getOneHotTopicCluster(id);
        if(htc == null) throw new Exception404(String.format("Cluster %s not found", id));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(htc));
    }

    @RequestMapping(value = "/clusters/{id}/hotTopics", method = RequestMethod.GET, params = {"title", "pageNum", "pageSize"})
    public RestApi getClusterHotTopics(@PathVariable String id, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "1") int pageSize, @RequestParam(value = "title") String title) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGESIZE) pageSize = MAX_PAGESIZE;
        HotTopicCluster htc = hotTopicService.getOneHotTopicCluster(id);
        List<String> htIds = htc.getHotTopicIds(title);
        if(htIds.size() == 0) throw new Exception404(String.format("Cluster %s doesn't have %s cluster", id, title));
        int si = pageSize * (pageNum - 1);
        int ei = si + pageSize;
        List<String> ids = htIds.subList(Math.min(htIds.size(), si), Math.min(htIds.size(), ei));
        return getHotTopicsById(Util.join(ids.toArray(new String[ids.size()]), ","));
    }
}
