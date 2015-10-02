package com.ucl.hottopic.repository;

import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.service.util.ArrayListPrintable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-17
 * Time: 下午10:45
 * To change this template use File | Settings | File Templates.
 */

public interface HotTopicRepository extends CrudRepository<HotTopic, String> {
    List<HotTopic> findByTimeBetween(Date from, Date to);
    @Query("{ 'id' : { $in: ?0 } }")
    List<HotTopic> findByIds(ArrayListPrintable<String> ids);
    Page<HotTopic> findByTimeBetween(Date from, Date to, Pageable pageable);
}
