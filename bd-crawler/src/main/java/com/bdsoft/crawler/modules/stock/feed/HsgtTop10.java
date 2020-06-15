package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 沪股通、深股通排名
 */
@Data
public class HsgtTop10 {

    @JSONField(name = "MarketType")
    private int marketType;

    @JSONField(name = "DetailDate")
    private String day;

    private int rank;

    @JSONField(name = "Code")
    private String code;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "Close")
    private BigDecimal close;

    @JSONField(name = "ChangePercent")
    private BigDecimal changePercent;

    /**
     * 沪股通买入金额
     */
    @JSONField(name = "HGTMRJE")
    private BigDecimal hgtIn;

    /**
     * 沪股通卖出金额
     */
    @JSONField(name = "HGTMCJE")
    private BigDecimal hgtOut;

    /**
     * 沪股通净买额 = 沪股通买入金额 - 沪股通卖出金额
     */
    @JSONField(name = "HGTJME")
    private BigDecimal hgtReal;

    /**
     * 沪股通成交金额
     */
    @JSONField(name = "HGTCJJE")
    private BigDecimal hgtDeal;

    /**
     * 深股通买入金额
     */
    @JSONField(name = "SGTMRJE")
    private BigDecimal sgtIn;

    /**
     * 深股通卖出金额
     */
    @JSONField(name = "SGTMCJE")
    private BigDecimal sgtOut;

    /**
     * 深股通净买额 = 深股通买入金额 - 深股通卖出金额
     */
    @JSONField(name = "SGTJME")
    private BigDecimal sgtReal;

    /**
     * 深股通成交金额
     */
    @JSONField(name = "SGTCJJE")
    private BigDecimal sgtDeal;

}
