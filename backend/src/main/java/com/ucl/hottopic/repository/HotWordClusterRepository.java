package com.ucl.hottopic.repository;

import com.ucl.hottopic.domain.HotWordCluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午11:07
 * To change this template use File | Settings | File Templates.
 */
public interface HotWordClusterRepository extends CrudRepository<HotWordCluster, String> {
    HotWordCluster findByStartAndEnd(Date start, Date end);
    Page<HotWordCluster> findAll(Pageable pageable);
    Page<HotWordCluster> findByEndLessThanEqual(Date end, Pageable pageable);
}
