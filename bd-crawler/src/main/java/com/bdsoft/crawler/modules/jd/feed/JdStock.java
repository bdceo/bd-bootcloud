package com.bdsoft.crawler.modules.jd.feed;

import lombok.Data;

/**
 * sku-库存
 */
@Data
public class JdStock {

    // 是否京东配货
    private Boolean isJDexpress;

    // 实际skuId
    private String realSkuId;

    // 库存状态名称
    private String StockStateName;

    // 价格信息
    private JdPrice jdPrice;

}
