package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.HotTopicCluster;
import com.ucl.hottopic.domain.HotWord;
import com.ucl.hottopic.domain.HotWordCluster;
import com.ucl.hottopic.domain.RestApi;
import com.ucl.hottopic.service.HotWordClusterService;
import com.ucl.hottopic.service.HotWordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */

@RestController
public class HotWordController {
    private Logger logger = Logger.getLogger(HotWordController.class);
    private static int MAX_PAGE_SIZE_HOTWORD = 30;
    private static int MAX_PAGE_SIZE_HOTWORDCLUSTER = 3;
    @Autowired
    private HotWordService hotWordService;
    @Autowired
    private HotWordClusterService hotWordClusterService;

    @RequestMapping(value = "/hotwords", method = RequestMethod.GET)
    public RestApi getHotWords(HttpServletRequest req) {
        return getHotWordsByEnd(req, DatatypeConverter.printDateTime(Calendar.getInstance()), 1, MAX_PAGE_SIZE_HOTWORD);
    }

    @RequestMapping(value = "/hotwords", method = RequestMethod.GET, params = {"before", "pageNum", "pageSize"})
    public RestApi getHotWordsByEnd(HttpServletRequest req, @RequestParam(value = "before") String date,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGE_SIZE_HOTWORD) pageSize = MAX_PAGE_SIZE_HOTWORD;
        Calendar cal = Calendar.getInstance();
        if(!date.equals("")) {
            cal = DatatypeConverter.parseDateTime(date);
        }
        List<HotWord> hws = hotWordService.getByEnd(cal.getTime(), pageNum, pageSize).getContent();
        logger.info(String.format("GET HOTWORD before %s page %d with page size %d", DatatypeConverter.printDateTime(cal), pageNum, pageSize));
        return new RestApi().setData(RestApi.getDataInstance().setHotWords(hws));
    }

    @RequestMapping(value = "/hotwordclusters", method = RequestMethod.GET)
    public RestApi getHotWordClusters(HttpServletRequest req) {
        return getHotWordClustersByEnd(req, DatatypeConverter.printDateTime(Calendar.getInstance()), 1, MAX_PAGE_SIZE_HOTWORD);
    }

    @RequestMapping(value = "/hotwordclusters", method = RequestMethod.GET, params = {"before", "pageNum", "pageSize"})
    public RestApi getHotWordClustersByEnd(HttpServletRequest req, @RequestParam(value = "before") String date,
                                    @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "1") int pageSize) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGE_SIZE_HOTWORDCLUSTER) pageSize = MAX_PAGE_SIZE_HOTWORDCLUSTER;
        Calendar cal = Calendar.getInstance();
        if(!date.equals("")) {
            cal = DatatypeConverter.parseDateTime(date);
        }
        List<HotWordCluster> hws = hotWordClusterService.getClusterPage(cal.getTime(), pageNum, pageSize).getContent();
        for(HotWordCluster c : hws) {
            Collections.sort(c.getClusters(), new Comparator<HotTopicCluster.OneCluster>() {
                @Override
                public int compare(HotTopicCluster.OneCluster o1, HotTopicCluster.OneCluster o2) {
                    return o1.getScore() > o2.getScore() ? -1 : o1.getScore() == o2.getScore() ? 0 : 1;
                }
            });
        }
        logger.info(String.format("GET HOTWORDCLUSTER before %s page %d with page size %d", DatatypeConverter.printDateTime(cal), pageNum, pageSize));
        return new RestApi().setData(RestApi.getDataInstance().setHotWordClusters(hws));
    }
}
