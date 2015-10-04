package com.ucl.hottopic.config;

import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-4
 * Time: 下午1:15
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@EnableConfigurationProperties({MongoOptions.class})
public class MongoConfiguration {
    @Autowired
    private MongoOptions mongoOptions;

    @Bean
    public MongoClientOptions mongoClientOptions() {
        return MongoClientOptions.builder()
                .connectTimeout(mongoOptions.getConnectTimeout())
                .heartbeatFrequency(mongoOptions.getHeartbeatFrequency())
                .maxConnectionIdleTime(mongoOptions.getMaxConnectionIdleTime())
                .socketKeepAlive(mongoOptions.isSocketKeepAlive())
                .build();
    }
}
