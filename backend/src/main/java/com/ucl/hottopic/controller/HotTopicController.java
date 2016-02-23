package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.*;
import com.ucl.hottopic.exception.Exception404;
import com.ucl.hottopic.service.HotTopicService;
import com.ucl.hottopic.service.SerpService;
import com.ucl.hottopic.service.cluster.Cluster;
import com.ucl.hottopic.service.util.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
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
    private static int MAX_PAGE_SIZE_HOTTOPIC = 30;
    private static int MAX_PAGE_SIZE_CLUSTER = 3;
    private static int MAX_CLUSTER_NUM = 50;
    private static int MAX_CLUSTER_ALIAS_NUM = 20;
    private static Set<Integer> scopeAllowed = new HashSet<Integer>(Arrays.asList(24, 2));

    private HotTopicCluster toExternal(HotTopicCluster htc, HttpServletRequest req) {
        if(htc == null) return null;
        String c = req.getParameter("c");
        int start = 0, end = MAX_CLUSTER_NUM;
        if(c != null) {
            int p = Integer.parseInt(c) <= 0 ? 1 : Integer.parseInt(c);
            end = p * MAX_CLUSTER_NUM;
            start = end - MAX_CLUSTER_NUM;
        }
        return htc.toExternal(start,end, MAX_CLUSTER_ALIAS_NUM);
    }

    @Autowired
    private HotTopicService hotTopicService;
    @Autowired
    private SerpService serpService;
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
        if(pageSize > MAX_PAGE_SIZE_HOTTOPIC) pageSize = MAX_PAGE_SIZE_HOTTOPIC;
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
        return getHotTopicsByTime(DatatypeConverter.printDateTime(start), DatatypeConverter.printDateTime(now), 1, MAX_PAGE_SIZE_HOTTOPIC);
    }

    @RequestMapping(value = "/hottopics/{id}", method = RequestMethod.GET)
    public RestApi getHotTopicById(@PathVariable String id) {
        HotTopic ht = hotTopicService.getOneHotTopic(id);
        if(ht == null) throw new Exception404(String.format("Hot topic %s not found", id));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopics(ht));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET, params = {"endTime", "scope"})
    public RestApi getClustersByTime(HttpServletRequest req, @RequestParam(value = "endTime") String endTime, @RequestParam(value = "scope", defaultValue = "24") int scope) {
        Calendar now = Calendar.getInstance();
        if(!endTime.equals("")) {
            now.setTime(DatatypeConverter.parseDateTime(endTime).getTime());
        }
        Calendar end = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), 0, 0);

        int rawScope = scope;
        if(!scopeAllowed.contains(scope)) {
            int minDis = Integer.MAX_VALUE;
            for(int al : scopeAllowed) {
                if(Math.abs(al - rawScope) < minDis) {
                    minDis = Math.abs(al - rawScope);
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
        HotTopicCluster htce = toExternal(htc, req);
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(htce));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET, params = {"before", "pageNum", "pageSize"})
    public RestApi getClusterList(HttpServletRequest req, @RequestParam(value = "before") String date, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGE_SIZE_CLUSTER) pageSize = MAX_PAGE_SIZE_CLUSTER;
        Calendar cal = Calendar.getInstance();
        if(!date.equals("")) {
            cal = DatatypeConverter.parseDateTime(date);
        }
        List<HotTopicCluster> clusters = hotTopicService.getClusterPage(cal.getTime(), pageNum, pageSize);
        List<HotTopicCluster> clustersExt = new ArrayList<HotTopicCluster>();
        for(HotTopicCluster htc : clusters) {
            clustersExt.add(toExternal(htc, req));
        }
        logger.info(String.format("GET CLUSTER before %s page %d with page size %d", DatatypeConverter.printDateTime(cal), pageNum, pageSize));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(clustersExt));
    }

    @RequestMapping(value = "/clusters", method = RequestMethod.GET)
    public RestApi getCluster(HttpServletRequest req) {
        return getClusterList(req, DatatypeConverter.printDateTime(Calendar.getInstance()), 1, MAX_PAGE_SIZE_CLUSTER);
    }

    @RequestMapping(value = "/clusters/{id}", method = RequestMethod.GET)
    public RestApi getClusterById(HttpServletRequest req, @PathVariable String id) {
        HotTopicCluster htc = hotTopicService.getOneHotTopicCluster(id);
        if(htc == null) throw new Exception404(String.format("Cluster %s not found", id));
        return new RestApi().setData(RestApi.getDataInstance().setHotTopicClusters(toExternal(htc, req)));
    }

    @RequestMapping(value = "/clusters/{id}/hottopics", method = RequestMethod.GET, params = {"title", "pageNum", "pageSize"})
    public RestApi getClusterHotTopics(@PathVariable String id, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "1") int pageSize, @RequestParam(value = "title") String title) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGE_SIZE_HOTTOPIC) pageSize = MAX_PAGE_SIZE_HOTTOPIC;
        HotTopicCluster htc = hotTopicService.getOneHotTopicCluster(id);
        if(htc == null) throw new Exception404(String.format("Cluster %s not found", id));
        List<String> htIds = htc.getHotTopicIds(title);
        if(htIds.size() == 0) throw new Exception404(String.format("Cluster %s doesn't have %s cluster", id, title));
        int si = pageSize * (pageNum - 1);
        int ei = si + pageSize;
        List<String> ids = htIds.subList(Math.min(htIds.size(), si), Math.min(htIds.size(), ei));
        return getHotTopicsById(Util.join(ids.toArray(new String[ids.size()]), ","));
    }

}
