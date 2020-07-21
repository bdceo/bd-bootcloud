package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 港股通-深-排行
 */
@Data
public class GgtSTop10 extends HSgtTopBase {

    /**
     * 港股通-深-买入
     */
    @JSONField(name = "GGTSMRJE")
    private BigDecimal inAmount;
    /**
     * 港股通-深-卖出
     */
    @JSONField(name = "GGTSMCJE")
    private BigDecimal outAmount;
    /**
     * 港股通-深-净买额 = 买入 - 卖出
     */
    @JSONField(name = "GGTSJME")
    private BigDecimal realAmount;
    /**
     * 港股通-深-成交额
     */
    @JSONField(name = "GGTSCJJE")
    private BigDecimal dealAmount;
}
