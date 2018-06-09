package com.scraper.Impl;

import com.scraper.api.App;
import com.scraper.api.ScraperConstants;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AppParser {


    public App parseAppdetails(String html, String appId) throws ParseException {

        Matcher scriptMatcher = Pattern.compile(ScraperConstants.SCRIPT_REGEX).matcher(html);
        scriptMatcher.find();

        Matcher valueMatcher = Pattern.compile(ScraperConstants.VALUE_REGEX).matcher(scriptMatcher.group());
        valueMatcher.find();

        String valueString = valueMatcher.group();
        String finalString = formatString(valueString);

        JSONParser parser = new JSONParser();
        Object array = parser.parse(finalString);

        App app = createApp(appId, (JSONArray) array);

        return app;

    }

    private App createApp(String appId, JSONArray array) {
        String title = parseResult(ScraperConstants.TITLE, array);
        String email = parseResult(ScraperConstants.EMAIL, array);
        String icon = parseResult(ScraperConstants.ICON, array);
        return new App(appId, title, icon, email);
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
        String trim = valueString.substring(ScraperConstants.PREFIX.length() - 1, valueString.length() + 1 - ScraperConstants.SUFFIX.length());
        return trim;
    }


}
