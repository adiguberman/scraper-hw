package com.scraper.Impl;

import com.scraper.api.ProxyString;
import com.scraper.api.ScraperConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Service

public class ProxyHolder {

    private static final Logger logger = LogManager.getLogger(ProxyHolder.class);
    private final String proxiesFilePath;
    private final boolean useProxy;
    private ConcurrentLinkedQueue<ProxyString> proxies = new ConcurrentLinkedQueue<>();

    @Autowired
    public ProxyHolder(@Value("${proxies.file.path}") final String proxiesFilePath, @Value("${use.proxy}") final String useProxy) throws Exception {
        this.proxiesFilePath = proxiesFilePath;
        this.useProxy = Boolean.parseBoolean(useProxy);
        logger.info("useProxy: " + useProxy);
        logger.info("proxiesFilePath: " + proxiesFilePath);

        if (this.useProxy) {
            loadProxies();
            if (proxies.isEmpty()) {
                logger.error("No proxies were found shutting down");
                throw new RuntimeException("No proxies were found shutting down");
            }
        }
    }

    private void loadProxies() throws Exception {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(proxiesFilePath))) {

            while ((line = br.readLine()) != null) {
                String[] proxyStringString = line.split(ScraperConstants.DELIMITER);
                ProxyString proxyString = new ProxyString(proxyStringString[0], Integer.parseInt(proxyStringString[1]));
                logger.info("adding proxy: " + proxyString);
                proxies.add(proxyString);
            }

        } catch (Exception e) {
            throw (new Exception("cant read proxies file", e));
        }
    }

    public ProxyString poll() throws InterruptedException {
        ProxyString proxyString = null;
        if (useProxy) {
            proxyString = proxies.poll();
            while (proxyString == null) {
                logger.error("proxyString == null, This should not happen, number of proxies should be equal to number of threads ");
                TimeUnit.SECONDS.sleep(ScraperConstants.TIMEOUT);
                proxyString = proxies.poll();
            }
        }
        return proxyString;
    }

    public void add(ProxyString proxyString) {
        if (useProxy) {
            proxies.add(proxyString);
        }
    }
}
