package com.bdsoft.crawler.modules.jd.feed;

import lombok.Data;

/**
 * sku-价格
 */
@Data
public class JdPrice {

    // skuId
    private String id;

    private String op;
    private String m;
    // price
    private String p;

}
