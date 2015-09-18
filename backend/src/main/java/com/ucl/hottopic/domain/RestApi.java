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
        private List<TopicClusterExt> topicClusterExts = new ArrayList<TopicClusterExt>();

        public Data(){
            this.hotTopics = new ArrayList<HotTopic>();
        }

        public List<HotTopic> getHotTopics() {
            return hotTopics;
        }

        public List<TopicClusterExt> getTopicClusterExts() {
            return topicClusterExts;
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

        public Data setTopicClusterExts(List<TopicClusterExt> topicClusterExts) {
            this.topicClusterExts = topicClusterExts;
            return this;
        }

        public Data setTopicClusterExts(TopicClusterExt topicClusterExt) {
            this.topicClusterExts = new ArrayList<TopicClusterExt>();
            this.topicClusterExts.add(topicClusterExt);
            return this;
        }
    }
}
