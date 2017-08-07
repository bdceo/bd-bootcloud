package com.bdsoft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * 属性文件映射，通过Value注解，对应
 */
@Component
public class InfoConfig {

    @Value("${info.app.name}")
    private String appName;
    @Value("${info.app.auther}")
    private String appAuther;
    @Value("${info.dev.version}")
    private String devVersion;
    @Value("${info.dev.tools}")
    private String devTools;

    @Override
    public String toString() {
        return "InfoConfig{" +
                "appName='" + appName + '\'' +
                ", appAuther='" + appAuther + '\'' +
                ", devVersion='" + devVersion + '\'' +
                ", devTools='" + devTools + '\'' +
                '}';
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppAuther() {
        return appAuther;
    }

    public void setAppAuther(String appAuther) {
        this.appAuther = appAuther;
    }

    public String getDevVersion() {
        return devVersion;
    }

    public void setDevVersion(String devVersion) {
        this.devVersion = devVersion;
    }

    public String getDevTools() {
        return devTools;
    }

    public void setDevTools(String devTools) {
        this.devTools = devTools;
    }
}
