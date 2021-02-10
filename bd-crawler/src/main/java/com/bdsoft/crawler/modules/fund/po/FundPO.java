package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

import java.util.Date;

/**
 * 基金基础信息
 */
@Data
public class FundPO {

    /**
     * 基金标识：代码，简称，全称，拼音
     */
    private String code;
    private String name;
    private String fullName;
    private String py;

    /**
     * 类型：股票、债券、指数、混合
     */
    private String type;

    /**
     * 基金规模：单位亿
     */
    private float gm;

    /**
     * 发行日期
     */
    private Date fxDate;

    /**
     * 基金公司：名称、代码
     */
    private String company;
    private String companyCode;

    public FundPO() {
    }

    public FundPO(String code) {
        this.code = code;
    }

    public FundPO(String code, String name, String py) {
        this.code = code;
        this.name = name;
        this.py = py;
    }
}
