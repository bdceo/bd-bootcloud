package com.bdsoft.crawler.modules.stock.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.bdsoft.crawler.modules.stock.StockConfig;
import lombok.Data;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * 流通股东信息
 */
@Data
public class StockHolderPO {

    @JSONField(name = "SHAREHDCODE")
    private String code;
    @JSONField(name = "SHAREHDNAME")
    private String name;
    @JSONField(name = "SHAREHDTYPE")
    private String type;
    @JSONField(name = "LTAG")
    private BigDecimal mvTotal;

    public String getInfoUrl() {
        return MessageFormat.format(StockConfig.STOCK_INDEX, code);
    }

}
