package com.scraper.test;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAppMainFlow {

    public static final String PREFIX = "return ";
    public static final String SUFFIX = "}}\\);<";
    public static final String scriptRegex = ">AF_initDataCallback\\(\\{key: 'ds:4'[\\s\\S]*?</script";
    public static final String valueRegex = "return ([\\s\\S]*?)}}\\);<";
    //developerEmail: ['ds:4', 0, 12, 5, 2, 0],
    //icon: ['ds:4', 0, 12, 1, 3, 2],
    //title: ['ds:4', 0, 0, 0],
    public static final int[] EMAIL = {0, 12, 5, 2, 0};
    public static final int[] ICON = {0, 12, 1, 3, 2};
    public static final int[] TITLE = {0, 0, 0};


    @Test
    public void matcherTest() throws IOException, ParseException {
        String html = getHtml();

        Matcher scriptMatcher = Pattern.compile(scriptRegex).matcher(html);
        scriptMatcher.find();

        Matcher valueMatcher = Pattern.compile(valueRegex).matcher(scriptMatcher.group());
        valueMatcher.find();

        JSONParser parser = new JSONParser();


        String valueString = valueMatcher.group();
        String finalString = formatString(valueString);

        Object obj = parser.parse(finalString);
        JSONArray array = (JSONArray) obj;

        String title = parseResult(TITLE, array);
        String email = parseResult(EMAIL, array);
        String icon = parseResult(ICON, array);

        System.out.println(title);
        System.out.println(email);
        System.out.println(icon);
    }

    private String parseResult(int[] mapping, JSONArray array) {
        JSONArray arrayForCalc = array;
        int i;
        for (i = 0; i < mapping.length - 1; i++) {
            arrayForCalc = (JSONArray) arrayForCalc.get(mapping[i]);
        }
        int index = mapping[i];
        return arrayForCalc.get(index).toString();
    }

    private String formatString(String valueString) {
        String trim = valueString.substring(PREFIX.length() - 1, valueString.length() + 1 - SUFFIX.length());
        return trim;
    }

    private String getHtml() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {

            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return statusCode != HttpStatus.NOT_FOUND && super.hasError(statusCode);
            }
        });
        ResponseEntity<String> entity = restTemplate.getForEntity("https://play.google.com/store/apps/details?id=com.doodle.turboracing3dd", String.class);
        return entity.getBody();
    }
}
