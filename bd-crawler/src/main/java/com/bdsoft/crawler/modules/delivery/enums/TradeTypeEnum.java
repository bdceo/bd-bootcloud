package com.bdsoft.crawler.modules.delivery.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 交易类型
 */
public enum TradeTypeEnum {

    TP1(1, "证券买入"),
    TP2(2, "证券卖出"),
    TP3(3, "申购配号");

    private Integer code;
    private String name;

    TradeTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Integer getTypeByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("少参数");
        }
        for (TradeTypeEnum en : TradeTypeEnum.values()) {
            if (en.getName().equals(name)) {
                return en.getCode();
            }
        }
        throw new IllegalArgumentException("未定义交易类型");
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
