package com.scraper.Impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ScraperRestController {
    public static final String HTTPS_PLAY_GOOGLE_COM_STORE_APPS_DETAILS_ID = "https://play.google.com/store/apps/details?id=";
    private final ScraperService scraperService;
    Logger logger = LogManager.getLogger(this.getClass());


    @Autowired
    public ScraperRestController(ScraperService scraperService) {
        this.scraperService = scraperService;

    }

    @RequestMapping("/scraper/{appid}")
    @ResponseStatus(value = HttpStatus.OK)
    public String parseAppDetails(@PathVariable("appid") String appid) throws ParseException, InterruptedException {
        logger.info("add details of app with id " + appid);
        scraperService.addAppDetailsToOutput(appid);
        return appid;
    }


}

