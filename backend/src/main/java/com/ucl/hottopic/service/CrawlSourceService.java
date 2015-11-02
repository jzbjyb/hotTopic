package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.CrawlSource;
import com.ucl.hottopic.repository.CrawlSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */

@Service
public class CrawlSourceService {
    @Autowired
    private CrawlSourceRepository crawlSourceRepository;

    public CrawlSource get(String id) {
        return crawlSourceRepository.findOne(id);
    }
}
