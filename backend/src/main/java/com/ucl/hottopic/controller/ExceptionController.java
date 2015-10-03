package com.ucl.hottopic.controller;

import com.ucl.hottopic.domain.RestApi;
import com.ucl.hottopic.exception.Exception404;
import com.ucl.hottopic.exception.Exception500;
import com.ucl.hottopic.service.util.Util;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-18
 * Time: 下午11:44
 * To change this template use File | Settings | File Templates.
 */

@ControllerAdvice
public class ExceptionController {
    private Logger logger = Logger.getLogger(ExceptionController.class);

    public void LogException(Exception e) {
        int index = e.getClass().toString().lastIndexOf('.');
        logger.error(Util.join(new String[]{"@EXCEPTION", e.getClass().toString().substring(index + 1), e.getMessage()}, " "));
        logger.error("", e);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({Exception404.class})
    public @ResponseBody RestApi notFound(HttpServletRequest req, HttpServletResponse res, Exception ex) {
        LogException(ex);
        return new RestApi(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception500.class})
    public @ResponseBody RestApi serverError(HttpServletRequest req, HttpServletResponse res, Exception ex) {
        LogException(ex);
        return new RestApi(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // unknown exception
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public @ResponseBody RestApi unknown(HttpServletRequest req, HttpServletResponse res, Exception ex) {
        LogException(ex);
        return new RestApi(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}