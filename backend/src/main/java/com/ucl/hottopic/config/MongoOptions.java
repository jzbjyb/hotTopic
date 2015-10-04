package com.ucl.hottopic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 * User: jzb
 * Date: 15-10-4
 * Time: 下午1:12
 * To change this template use File | Settings | File Templates.
 */

@ConfigurationProperties(prefix = "ucl.mongodb.options")
public class MongoOptions {
    private boolean autoConnectRetry = false;
    private int connectTimeout = 10 * 1000;
    private int heartbeatFrequency = 1 * 1000;
    private int maxConnectionIdleTime = 30 * 1000;
    private boolean socketKeepAlive = true;

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public int getMaxConnectionIdleTime() {
        return maxConnectionIdleTime;
    }

    public int getHeartbeatFrequency() {
        return heartbeatFrequency;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public boolean isAutoConnectRetry() {
        return autoConnectRetry;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public void setMaxConnectionIdleTime(int maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    public void setHeartbeatFrequency(int heartbeatFrequency) {
        this.heartbeatFrequency = heartbeatFrequency;
    }

    public void setAutoConnectRetry(boolean autoConnectRetry) {
        this.autoConnectRetry = autoConnectRetry;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
