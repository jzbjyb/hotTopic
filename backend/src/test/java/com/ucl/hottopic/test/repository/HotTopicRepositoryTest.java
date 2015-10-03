package com.ucl.hottopic.test.repository;

import com.ucl.hottopic.Application;
import com.ucl.hottopic.domain.HotTopic;
import com.ucl.hottopic.service.HotTopicService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-19
 * Time: 下午2:20
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class HotTopicRepositoryTest {
    @Autowired
    private HotTopicService hotTopicService;

    @Test
    public void testFindByIds() {
        List<String> ids = new ArrayList<>();
        Page<HotTopic> hts = hotTopicService.getLatestHotTopics(1, 5);
        for(HotTopic ht : hts.getContent()) {
            ids.add(ht.getId());
        }
        Collections.sort(ids);
        List<HotTopic> htsRe = hotTopicService.getMultiHotTopic(ids);
        List<String> idsFind = new ArrayList<>();
        for(HotTopic ht : htsRe) {
            idsFind.add(ht.getId());
        }
        Collections.sort(idsFind);
        for(int i=0; i<idsFind.size(); i++) {
            Assert.assertEquals(ids.get(i), idsFind.get(i));
        }
    }
}
