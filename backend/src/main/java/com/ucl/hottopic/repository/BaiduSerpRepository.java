package com.ucl.hottopic.repository;

import com.ucl.hottopic.domain.BaiduSerp;
import com.ucl.hottopic.service.util.ArrayListPrintable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */

public interface BaiduSerpRepository extends CrudRepository<BaiduSerp, String> {
    @Query("{ 'id' : { $in: ?0 } }")
    List<BaiduSerp> findByIds(ArrayListPrintable<String> ids);
}
