package com.ucl.hottopic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-2
 * Time: 下午4:52
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = "BaiduSerp")
public class BaiduSerp {
    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date time;
    private String crawlUrl;
    private String cachedSourceId;
    private String query;
    private List<SerpResult> results;

    public String getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getCrawlUrl() {
        return crawlUrl;
    }

    public String getCachedSourceId() {
        return cachedSourceId;
    }

    public String getQuery() {
        return query;
    }

    public List<SerpResult> getResults() {
        return results;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setCrawlUrl(String crawlUrl) {
        this.crawlUrl = crawlUrl;
    }

    public void setCachedSourceId(String cachedSourceId) {
        this.cachedSourceId = cachedSourceId;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setResults(List<SerpResult> results) {
        this.results = results;
    }
}

class SerpResult {
    private String link;
    private String title;
    private String title_html;
    private String abs;
    private String abs_html;
    private int rank;

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_html() {
        return title_html;
    }

    public String getAbs() {
        return abs;
    }

    public String getAbs_html() {
        return abs_html;
    }

    public int getRank() {
        return rank;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle_html(String title_html) {
        this.title_html = title_html;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public void setAbs_html(String abs_html) {
        this.abs_html = abs_html;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

