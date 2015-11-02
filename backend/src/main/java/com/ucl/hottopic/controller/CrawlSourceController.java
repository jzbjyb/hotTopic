package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.CrawlSource;
import com.ucl.hottopic.domain.RestApi;
import com.ucl.hottopic.exception.Exception404;
import com.ucl.hottopic.service.CrawlSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */

@RestController
public class CrawlSourceController {
    @Autowired
    private CrawlSourceService crawlSourceService;

    @RequestMapping(value = "/source/{id}", method = RequestMethod.GET)
    public RestApi getSource(@PathVariable String id) {
        CrawlSource cs = crawlSourceService.get(id);
        if(cs == null) throw new Exception404(String.format("crawl source %s not found", id));
        return new RestApi().setData(RestApi.getDataInstance().setCrawlSources(cs));
    }
}
