package com.ucl.hottopic.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-17
 * Time: 下午10:29
 * To change this template use File | Settings | File Templates.
 */

@Document(collection = "HotTopic")
public class HotTopic {
    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date time;
    private String crawlUrl;
    private String cachedSourceId;
    private String rawId;
    private String title;
    private String desc;
    private List<String> queryWord;
    private List<String> keyWord;
    private List<String> category;
    private String imgUrl;
    private String targetUrl;
    private int rank;
    private String baiduSerpId;

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

    public String getRawId() {
        return rawId;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public List<String> getQueryWord() {
        return queryWord;
    }

    public List<String> getKeyWord() {
        return keyWord;
    }

    public List<String> getCategory() {
        return category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public int getRank() {
        return rank;
    }

    public String getBaiduSerpId() {
        return baiduSerpId;
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

    public void setRawId(String rawId) {
        this.rawId = rawId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setQueryWord(List<String> queryWord) {
        this.queryWord = queryWord;
    }

    public void setKeyWord(List<String> keyWord) {
        this.keyWord = keyWord;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setBaiduSerpId(String baiduSerpId) {
        this.baiduSerpId = baiduSerpId;
    }
}
