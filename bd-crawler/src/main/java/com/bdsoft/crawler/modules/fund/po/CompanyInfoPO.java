package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

import java.util.Date;

/**
 * 基金公司
 */
@Data
public class CompanyInfoPO {

    /**
     * 公司标识、名称、注册地址
     */
    private String code;
    private String name;
    private String regAddress;

    /**
     * 成立日期
     */
    private Date setupDate;

    /**
     * 管理基金数量
     */
    private int fundTotal;

    /**
     * 基金经理人数
     */
    private int managerTotal;

    /**
     * 管理规模：亿元
     */
    private float fundScale;

    public CompanyInfoPO() {
    }

    public CompanyInfoPO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
