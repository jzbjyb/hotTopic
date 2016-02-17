package com.ucl.hottopic.repository;

import com.ucl.hottopic.domain.HotTopicCluster;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-1
 * Time: 下午8:36
 * To change this template use File | Settings | File Templates.
 */
public interface HotTopicClusterRepository extends CrudRepository<HotTopicCluster, String> {
    HotTopicCluster findByStartAndEnd(Date start, Date end);
    Page<HotTopicCluster> findAll(Pageable pageable);
    Page<HotTopicCluster> findByEndLessThanEqual(Date end, Pageable pageable);
}
