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
     * 产品类型
     */
    private String type;

    /**
     * 基金公司
     */
    private String company;

    public FundRePO(String indexCode, String code, String name, Date setupDate, String type, String company) {
        super(code, name);
        this.indexCode = indexCode;
        this.setupDate = setupDate;
        this.type = type;
        this.company = company;
    }

}
