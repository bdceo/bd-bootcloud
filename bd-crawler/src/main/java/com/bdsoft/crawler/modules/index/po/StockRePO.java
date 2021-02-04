package com.bdsoft.crawler.modules.index.po;

import lombok.Data;

/**
 * 成分股
 */
@Data
public class StockRePO extends CommonPO {

    /**
     * 指数编码
     */
    private String indexCode;

    /**
     * 中证行业
     */
    private String industry;

    /**
     * 权重
     */
    private float weight;

    public StockRePO(String indexCode, String code, String name, String industry, float weight) {
        super(code, name);
        this.indexCode = indexCode;
        this.industry = industry;
        this.weight = weight;
    }

}
