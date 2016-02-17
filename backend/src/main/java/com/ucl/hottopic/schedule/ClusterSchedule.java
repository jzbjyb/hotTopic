package com.ucl.hottopic.schedule;

import com.ucl.hottopic.repository.HotTopicClusterRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-12-4
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */

/*
@Component
@Profile("app")
class ClusterSchedule {
    private Logger logger = Logger.getLogger(ClusterSchedule.class);
    @Autowired
    private HotTopicClusterRepository hotTopicClusterRepository;

    public ClusterSchedule() {
    }

    @Scheduled(fixedDelay = 500)
    public void retrieveCountry() {
        logger.info(String.format("Load HottopicCluster to Cache at %s", DatatypeConverter.printDateTime(Calendar.getInstance())));
        hotTopicClusterRepository.findByStartAndEnd()
    }

}   */
