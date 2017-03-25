package com.yue.Crawel.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Andrew on 16/5/9.
 */

public class IpPort {

    @Getter
    @Setter
    private String ip;

    @Getter
    @Setter
    private Integer port;

    public IpPort() {
    }

    public IpPort(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

}
