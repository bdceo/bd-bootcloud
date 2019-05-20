package com.bdsoft.crawler.enums;

/**
 * 基金类型
 */
public enum FundTypeEnum {

    STOCK(1, "股票型"),
    MIX(2, "混合型"),
    BOND(3, "债券型"),
    INDEX(4, "指数型");

    private Integer code;
    private String name;

    FundTypeEnum(Integer code, String name) {
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
