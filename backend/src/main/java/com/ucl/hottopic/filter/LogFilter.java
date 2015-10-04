package com.ucl.hottopic.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-4
 * Time: 下午7:54
 * To change this template use File | Settings | File Templates.
 */

public class LogFilter extends AbstractRequestLoggingFilter {
    private String logFormatter = "%s   %s   %s";

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return logger.isInfoEnabled();
    }

    /**
     * Writes a log message before the request is processed.
     */
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
        logger.info(String.format(logFormatter, request.getMethod().toUpperCase(), request.getRequestURL(), request.getQueryString()));
    }

    /**
     * Writes a log message after the request is processed.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}
