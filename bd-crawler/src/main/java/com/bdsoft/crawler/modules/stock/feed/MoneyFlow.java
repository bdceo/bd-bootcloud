package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资金流动数据
 */
@Data
public class MoneyFlow {

    /*
    * 资金净流入
     */
    @JSONField(name = "dayNetAmtIn")
    private BigDecimal dayIn;
    /**
     * 当日资金余额
     */
    @JSONField(name = "dayAmtRemain")
    private BigDecimal dayRemain;
    /**
     * 当日资金限额
     */
    @JSONField(name = "dayAmtThreshold")
    private BigDecimal dayLimit;

}
