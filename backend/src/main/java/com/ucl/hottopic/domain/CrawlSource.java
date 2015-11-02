package com.ucl.hottopic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */

@Document(collection = "CrawlSource")
public class CrawlSource {
    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date time;
    private String url;
    private int type;
    private String source;

    public String getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
