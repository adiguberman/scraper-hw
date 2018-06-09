package com.scraper.Impl;

import com.scraper.api.App;
import com.scraper.api.ProxyString;
import com.scraper.api.ScraperConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Service
public class ScraperService {

    private static final Logger logger = LogManager.getLogger(ScraperService.class);
    private static final Logger loggerOfAppDetails = LogManager.getLogger(ScraperConstants.APP_DETAILS_LOGGER_NAME);
    private final AppParser appParser;
    private final ProxyHolder proxyHolder;
    private final boolean useProxy;

    @Autowired
    public ScraperService(AppParser appParser, ProxyHolder proxyHolder, @Value("${use.proxy}") final String useProxy) {
        this.appParser = appParser;
        this.proxyHolder = proxyHolder;
        this.useProxy = Boolean.parseBoolean(useProxy);
        logger.info("useProxy: " + useProxy);
    }

    @Async("threadPoolTaskExecutor")
    public void addAppDetailsToOutput(String appid) throws ParseException, InterruptedException {
        ProxyString proxyString = proxyHolder.poll();
        try {
            RestTemplate restTemplate = getRestTemplate(proxyString);
            String url = ScraperRestController.HTTPS_PLAY_GOOGLE_COM_STORE_APPS_DETAILS_ID + appid;

            ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            if (entity.getStatusCode() != HttpStatus.NOT_FOUND) {
                App app = appParser.parseAppdetails(entity.getBody(), appid);
                loggerOfAppDetails.info(app);
            } else {
                loggerOfAppDetails.error(appid + " was not found in app store");
            }
        } finally {
            TimeUnit.SECONDS.sleep(ScraperConstants.TIMEOUT);
            proxyHolder.add(proxyString);
        }
    }


    private RestTemplate getRestTemplate(ProxyString proxyString) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return statusCode != HttpStatus.NOT_FOUND && super.hasError(statusCode);
            }
        });
        if (useProxy) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            InetSocketAddress address = new InetSocketAddress(proxyString.getHost(), proxyString.getPort());
            logger.info("using proxy: " + proxyString.getHost() + proxyString.getPort());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            factory.setProxy(proxy);
            restTemplate.setRequestFactory(factory);
        }
        return restTemplate;
    }


}
