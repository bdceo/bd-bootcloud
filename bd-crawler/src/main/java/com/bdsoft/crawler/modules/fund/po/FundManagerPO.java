package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

import java.util.Date;

/**
 * 基金经理
 */
@Data
public class FundManagerPO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 基金经理编号、名称
     */
    private String managerCode;
    private String managerName;

    /**
     * 管理时间
     */
    private Date start;
    private Date end;

    public FundManagerPO() {
    }

    public FundManagerPO(String code, Date start, Date end) {
        this.code = code;
        this.start = start;
        this.end = end;
    }
}
