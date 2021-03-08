package com.bdsoft.crawler.modules.delivery.vo;

import lombok.Data;

/**
 * 基金持仓成本数据
 */
@Data
public class FundCostViewVO {

    /**
     * 基金编号、名称
     */
    private String code;
    private String name;

    /**
     * 基金净值线数据：标签、净值
     */
    private String lineValLabels;
    private String lineValData;

    /**
     * 买入线数据
     */
    private String lineBuyData;

    /**
     * 成本线数据
     */
    private String lineCostData;

    public FundCostViewVO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
