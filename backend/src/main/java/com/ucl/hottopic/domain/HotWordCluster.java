package com.ucl.hottopic.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-11-17
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */

@Document(collection = "HotWordCluster")
public class HotWordCluster extends HotTopicCluster {
}
