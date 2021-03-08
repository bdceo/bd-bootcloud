package com.bdsoft.crawler.modules.delivery.enums;

/**
 * 交易类型
 */
public enum AccountTypeEnum {

    TP1("华泰证券"),
    TP2("东方财富"),
    TP3("富途牛牛"),
    TP4("海通证券"),
    TP5("支付宝");

    private String name;

    AccountTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
