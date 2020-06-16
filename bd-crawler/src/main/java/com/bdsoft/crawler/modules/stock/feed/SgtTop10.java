package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 深股通排行
 */
@Data
public class SgtTop10 extends HSgtTopBase {

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
