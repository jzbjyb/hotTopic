package com.ucl.hottopic.repository;

import com.ucl.hottopic.domain.HotWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */

public interface HotWordRepository extends CrudRepository<HotWord, String> {
    Page<HotWord> findByStartAndEnd(Date start, Date end, Pageable pageable);
    Page<HotWord> findAll(Pageable pageable);
    Page<HotWord> findByEndLessThanEqual(Date end, Pageable pageable);
}
