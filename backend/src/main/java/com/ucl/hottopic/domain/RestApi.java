package com.ucl.hottopic.domain;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-9-17
 * Time: 下午11:27
 * To change this template use File | Settings | File Templates.
 */

public class RestApi {
    private int code;
    private String message;
    private Data data;

    public RestApi(HttpStatus hs, String message) {
        this.code = hs.value();
        this.message = message;
        this.data = new Data();
    }

    public RestApi(HttpStatus hs) {
        this(HttpStatus.OK, HttpStatus.OK.getReasonPhrase());
    }

    public RestApi() {
        this(HttpStatus.OK);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public RestApi setData(Data data) {
        this.data = data;
        return this;
    }

    public RestApi setMessage(String message) {
        this.message = message;
        return this;
    }

    public RestApi setCode(int code) {
        this.code = code;
        return this;
    }

    public static Data getDataInstance() {
        return new RestApi(HttpStatus.OK).new Data();
    }

    public class Data {
        private List<HotTopic> hotTopics = new ArrayList<HotTopic>();
        private List<HotTopicCluster> hotTopicClusters = new ArrayList<HotTopicCluster>();
        private List<BaiduSerp> baiduSerps = new ArrayList<BaiduSerp>();
        private List<CrawlSource> crawlSources = new ArrayList<CrawlSource>();
        private List<HotWord> hotWords = new ArrayList<HotWord>();
        private List<HotWordCluster> hotWordClusters = new ArrayList<HotWordCluster>();

        public Data(){
            this.hotTopics = new ArrayList<HotTopic>();
        }

        public List<HotTopic> getHotTopics() {
            return hotTopics;
        }

        public List<HotTopicCluster> getHotTopicClusters() {
            return hotTopicClusters;
        }

        public List<BaiduSerp> getBaiduSerps() {
            return baiduSerps;
        }

        public List<CrawlSource> getCrawlSources() {
            return crawlSources;
        }

        public List<HotWord> getHotWords() {
            return hotWords;
        }

        public List<HotWordCluster> getHotWordClusters() {
            return hotWordClusters;
        }

        public Data setHotTopics(List<HotTopic> hotTopics) {
            this.hotTopics = hotTopics;
            return this;
        }

        public Data setHotTopics(HotTopic hotTopic) {
            this.hotTopics = new ArrayList<HotTopic>();
            this.hotTopics.add(hotTopic);
            return this;
        }

        public Data setCrawlSources(List<CrawlSource> crawlSources) {
            this.crawlSources = crawlSources;
            return this;
        }

        public Data setCrawlSources(CrawlSource crawlSource) {
            this.crawlSources = new ArrayList<CrawlSource>();
            this.crawlSources.add(crawlSource);
            return this;
        }

        public Data setBaiduSerps(List<BaiduSerp> baiduSerps) {
            this.baiduSerps = baiduSerps;
            return this;
        }

        public Data setBaiduSerps(BaiduSerp baiduSerp) {
            this.baiduSerps = new ArrayList<BaiduSerp>();
            this.baiduSerps.add(baiduSerp);
            return this;
        }

        public Data setHotTopicClusters(List<HotTopicCluster> hotTopicClusters) {
            this.hotTopicClusters = hotTopicClusters;
            return  this;
        }

        public Data setHotTopicClusters(HotTopicCluster hotTopicCluster) {
            this.hotTopicClusters = new ArrayList<HotTopicCluster>();
            this.hotTopicClusters.add(hotTopicCluster);
            return  this;
        }

        public Data setHotWords(List<HotWord> hotWords) {
            this.hotWords = hotWords;
            return this;
        }

        public Data setHotWords(HotWord hotWord) {
            this.hotWords = new ArrayList<HotWord>();
            this.hotWords.add(hotWord);
            return this;
        }

        public Data setHotWordClusters(List<HotWordCluster> hotWordClusters) {
            this.hotWordClusters = hotWordClusters;
            return this;
        }

        public Data setHotWordClusters(HotWordCluster hotWordCluster) {
            this.hotWordClusters = new ArrayList<HotWordCluster>();
            this.hotWordClusters.add(hotWordCluster);
            return this;
        }
    }
}
