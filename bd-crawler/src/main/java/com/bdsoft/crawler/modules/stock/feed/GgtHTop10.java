package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 港股通-沪-排行
 */
@Data
public class GgtHTop10 extends HSgtTopBase {

    /**
     * 港股通-沪-买入
     */
    @JSONField(name = "GGTHMRJE")
    private BigDecimal ggtHIn;
    /**
     * 港股通-沪-卖出
     */
    @JSONField(name = "GGTHMCJE")
    private BigDecimal ggtHOut;
    /**
     * 港股通-沪-净买额 = 买入 - 卖出
     */
    @JSONField(name = "GGTHJME")
    private BigDecimal ggtHReal;
    /**
     * 港股通-沪-成交额
     */
    @JSONField(name = "GGTHCJJE")
    private BigDecimal ggtHDeal;

}
