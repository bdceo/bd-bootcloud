package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

import java.util.Date;

/**
 * 基金持仓
 */
@Data
public class FundHoldPO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 方案日期
     */
    private Date dt;

    /**
     * 股票代码，名称
     */
    private String stockCode;
    private String stockName;

    /**
     * 占净值比例
     */
    private float jzhRate;
    /**
     * 持股数（万股）
     */
    private float holdStock;
    /**
     * 持仓市值（万元）
     */
    private float holdValue;


    public FundHoldPO(String code, Date dt) {
        this.code = code;
        this.dt = dt;
    }

}

