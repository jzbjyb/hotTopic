package com.ucl.hottopic.config;

import com.ucl.hottopic.filter.LogFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-4
 * Time: 下午7:48
 * To change this template use File | Settings | File Templates.
 */

@Configuration
public class LogConfiguration {
    @Bean
    public Filter logFilter() {
        LogFilter filter = new LogFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);
        return filter;
    }
}
