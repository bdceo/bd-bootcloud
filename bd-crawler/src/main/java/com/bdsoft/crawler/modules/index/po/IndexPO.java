package com.bdsoft.crawler.modules.index.po;

import com.bdsoft.crawler.modules.index.IndexConfig;
import lombok.Data;

import java.text.MessageFormat;

/**
 * 指数对象
 */
@Data
public class IndexPO {

    /**
     * 指数代码、名称
     */
    private String code;
    private String name;

    /**
     * 成分股数量
     */
    private Integer stockNum;

    /**
     * 系列：中证，上证，深证，新三板
     */
    private String serial;

    /**
     * 指数类型：综合，规模，行业，风格，主题，策略
     */
    private String type;

    /**
     * 资产类别：股票，债券，基金，期货
     */
    private String assets;

    /**
     * 地区覆盖：境内，境外
     */
    private String region;

    /**
     * 币种：人民币，港元，美元
     */
    private String currency;

    public String getIndexPage() {
        return MessageFormat.format(IndexConfig.INDEX_PAGE, code);
    }

    public String getIndexFundPage() {
        return MessageFormat.format(IndexConfig.INDEX_FUND_PAGE, name);
    }

}
