package com.scraper.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;


@PropertySource(value = "classpath:platstorescraperrunner.properties")
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static final int TIMEOUT = 2;
    Logger logger = LogManager.getLogger(this.getClass());
    @Value("${ids.file.path}")
    String idsFilePath;
    @Value("${url}")
    String url;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        String appid = "";
        try (BufferedReader br = new BufferedReader(new FileReader(idsFilePath))) {

            while ((appid = br.readLine()) != null) {
                RestTemplate restTemplate = new RestTemplate();
                appid = appid.trim();
                try {
                    if (!StringUtils.isEmpty(appid)) {
                        ResponseEntity<String> entity = restTemplate.getForEntity(url + "/" + appid, String.class);
                    }
                } catch (Exception e) {
                    logger.error("can't find app deatils to app " + appid);
                }
            }
        }
    }
}



