package com.bdsoft.crawler.modules.index.po;

import lombok.Data;

import java.util.Date;

/**
 * 指数产品
 */
@Data
public class FundRePO extends CommonPO {

    /**
     * 指数编码
     */
    private String indexCode;

    /**
     * 成立日期
     */
    private Date setupDate;

    /**
     * 基金类型：其他、股票型、混合型
     */
    private String type1;

    /**
     * 产品类型：指数基金、LOF、联接基金、
     */
    private String type2;

    /**
     * 基金公司
     */
    private String company;

    public FundRePO(String indexCode, String code, String name, Date setupDate, String type1, String type2, String company) {
        super(code, name);
        this.indexCode = indexCode;
        this.setupDate = setupDate;
        this.type1 = type1;
        this.type2 = type2;
        this.company = company;
    }

}
