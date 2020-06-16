package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 沪股通排行
 */
@Data
public class HgtTop10 extends HSgtTopBase {

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
}
