package com.bdsoft.crawler.modules.stock.feed;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

/**
 * 沪股通、深股通、港股通排名
 */
@Data
public class HSgtTopBase {

    @JSONField(name = "MarketType")
    private int marketType;

    @JSONField(name = "DetailDate")
    private String day;

    private String rank;
    private String rank1;

    @JSONField(name = "Code")
    private String code;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "Close")
    private BigDecimal close;

    @JSONField(name = "ChangePercent")
    private BigDecimal changePercent;

    public int getRank() {
        if (StringUtils.isNotEmpty(rank) && NumberUtils.isNumber(rank)) {
            return NumberUtils.createNumber(rank).intValue();
        }
        if (StringUtils.isNotEmpty(rank1) && NumberUtils.isNumber(rank1)) {
            return NumberUtils.createNumber(rank1).intValue();
        }
        return 0;
    }
}
