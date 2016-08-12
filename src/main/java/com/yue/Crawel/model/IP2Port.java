package com.yue.Crawel.model;

/**
 * Created by Andrew on 16/5/9.
 */
public class IP2Port {
    private String ip;
    private Integer port;

    public IP2Port() {
    }

    public IP2Port(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
