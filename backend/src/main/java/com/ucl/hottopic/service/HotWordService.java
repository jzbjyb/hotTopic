package com.ucl.hottopic.service;

import com.ucl.hottopic.domain.HotWord;
import com.ucl.hottopic.repository.HotWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */

@Service
public class HotWordService {
    @Autowired
    private HotWordRepository hotWordRepository;

    public HotWord getById(String id) {
        return hotWordRepository.findOne(id);
    }

    public Page<HotWord> getBySpan(Date start, Date end, int pageNum, int pageSize) {
        // sort according end then start
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(
                Arrays.asList(new Sort.Order(Sort.Direction.DESC, "end"), new Sort.Order(Sort.Direction.ASC, "start"), new Sort.Order(Sort.Direction.DESC, "word.trend"))
        ));
        return hotWordRepository.findByStartAndEnd(start, end, request);
    }

    public Page<HotWord> getByEnd(Date end, int pageNum, int pageSize) {
        // sort according end then start
        PageRequest request = new PageRequest(pageNum-1, pageSize, new Sort(
                Arrays.asList(new Sort.Order(Sort.Direction.DESC, "end"), new Sort.Order(Sort.Direction.ASC, "start"), new Sort.Order(Sort.Direction.DESC, "word.trend"))
        ));
        return hotWordRepository.findByEndLessThanEqual(end, request);
    }
}
