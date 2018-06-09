package com.scraper.api;

public class ScraperConstants {

    public static final int TIMEOUT = 10;
    public static final String PREFIX = "return ";
    public static final String SUFFIX = "}}\\);<";
    public static final String SCRIPT_REGEX = ">AF_initDataCallback\\(\\{key: 'ds:4'[\\s\\S]*?</script";
    public static final String VALUE_REGEX = "return ([\\s\\S]*?)}}\\);<";

    public static final int[] EMAIL = {0, 12, 5, 2, 0};
    public static final int[] ICON = {0, 12, 1, 3, 2};
    public static final int[] TITLE = {0, 0, 0};

    public static final String APP_DETAILS_LOGGER_NAME = "AppDetails";
    public static final String DELIMITER = ",";

}
