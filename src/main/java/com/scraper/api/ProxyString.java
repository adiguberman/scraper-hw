package com.scraper.api;

public class ProxyString {

    final private String host;

    final private Integer port;

    @Override
    public String toString() {
        return "ProxyString{" + "host='" + host + '\'' + ", port=" + port + '}';
    }

    public ProxyString(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
