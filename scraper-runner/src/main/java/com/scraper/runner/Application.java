package com.scraper.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;


@PropertySource(value = "classpath:platstorescraperrunner.properties")
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Value("${ids.file.path}")
    String idsFilePath;
    @Value("${url}")
    String url;

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader(idsFilePath))) {

            while ((line = br.readLine()) != null) {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> entity = restTemplate.getForEntity(url + "/" + line, String.class);
            }

        } catch (Exception e) {
            throw (new Exception("something went wrong", e));
        }

    }
}



