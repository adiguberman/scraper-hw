package com.scraper.api;

public class App {

    private final String appId;
    private final String title;
    private final String icon;
    private final String developerEmail;

    public App(String appId, String title, String icon, String developerEmail) {
        this.appId = appId;
        this.title = title;
        this.icon = icon;
        this.developerEmail = developerEmail;
    }

    public String getAppId() {
        return appId;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }


    @Override
    public String toString() {
        return "App{" + "appId='" + appId + '\'' + ", title='" + title + '\'' + ", icon='" + icon + '\'' + ", developerEmail='" + developerEmail + '\'' + '}';
    }


}
