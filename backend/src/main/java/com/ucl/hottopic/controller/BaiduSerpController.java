package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.BaiduSerp;
import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.domain.HotTopicCluster;
import com.ucl.hottopic.domain.RestApi;
import com.ucl.hottopic.exception.Exception404;
import com.ucl.hottopic.service.HotTopicService;
import com.ucl.hottopic.service.SerpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午6:22
 * To change this template use File | Settings | File Templates.
 */

@RestController
public class BaiduSerpController {
    private static int MAX_PAGE_SIZE_BAIDUSERP = 30;

    @Autowired
    private HotTopicService hotTopicService;
    @Autowired
    private SerpService serpService;

    @RequestMapping(value = "/clusters/{id}/baiduserps", method = RequestMethod.GET, params = {"title", "pageNum", "pageSize"})
    public RestApi getClusterBaiduSerps(@PathVariable String id, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "1") int pageSize, @RequestParam(value = "title") String title) {
        if(pageNum <= 0) pageNum = 1;
        if(pageSize <= 0) pageSize = 1;
        if(pageSize > MAX_PAGE_SIZE_BAIDUSERP) pageSize = MAX_PAGE_SIZE_BAIDUSERP;
        HotTopicCluster htc = hotTopicService.getOneHotTopicCluster(id);
        if(htc == null) throw new Exception404(String.format("Cluster %s not found", id));
        List<String> htIds = htc.getHotTopicIds(title);
        if(htIds.size() == 0) throw new Exception404(String.format("Cluster %s doesn't have %s cluster", id, title));
        int si = pageSize * (pageNum - 1);
        int ei = si + pageSize;
        List<String> ids = htIds.subList(Math.min(htIds.size(), si), Math.min(htIds.size(), ei));
        List<HotTopic> hts = hotTopicService.getMultiHotTopic(ids);
        List<String> serpIds = serpService.getBaiduSerpIds(hts);
        List<BaiduSerp> serps = serpService.getMultiBaiduSerps(serpIds);
        return new RestApi().setData(RestApi.getDataInstance().setBaiduSerps(serps));
    }

    @RequestMapping(value = "/hottopics/{id}/baiduserp", method = RequestMethod.GET)
    public RestApi getHotTopicSerp(@PathVariable String id) {
        HotTopic ht = hotTopicService.getOneHotTopic(id);
        if(ht == null) throw new Exception404(String.format("Hot topic %s not found", id));
        if(ht.getBaiduSerpId() == null) throw new Exception404(String.format("Hot topic %s doesn't have baidu serp", id));
        BaiduSerp bs = serpService.getOneBaiduSerp(ht.getBaiduSerpId());
        if(bs == null) throw new Exception404(String.format("Baidu serp %s not found", ht.getBaiduSerpId()));
        return new RestApi().setData(RestApi.getDataInstance().setBaiduSerps(bs));
    }
}
