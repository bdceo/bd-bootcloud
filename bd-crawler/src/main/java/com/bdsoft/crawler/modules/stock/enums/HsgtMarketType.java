package com.bdsoft.crawler.modules.stock.enums;

/**
 * 市场类型
 */
public enum HsgtMarketType {

    HGT(1, "沪股通"),
    SHGT(3, "深股通"),
    GHGT(2, "港(沪)股通"),
    GSHGT(4, "港(深)股通");

    private int code;
    private String name;

    HsgtMarketType(int code, String name) {
        this.code = code;
        this.name = name;
    }


}
