package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

import java.util.Date;

/**
 * 基金持仓-债券
 */
@Data
public class FundBondPO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 方案日期
     */
    private Date dt;

    /**
     * 债券代码，名称
     */
    private String bondCode;
    private String bondName;

    /**
     * 占净值比例
     */
    private float valueRate;

    /**
     * 持仓市值（万元）
     */
    private float marketValue;

    public FundBondPO() {
    }

    public FundBondPO(String code, Date dt) {
        this.code = code;
        this.dt = dt;
    }

}
