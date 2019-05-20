package com.bdsoft.crawler.enums;

/**
 * 增长类型
 */
public enum GrowthType {

    UP(1, "涨"),
    SAME(0, "持平"),
    DOWN(-1, "跌");

    private Integer code;
    private String name;

    GrowthType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
