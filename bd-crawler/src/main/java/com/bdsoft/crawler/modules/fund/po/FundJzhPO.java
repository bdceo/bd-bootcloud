package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

import java.util.Date;

/**
 * 基金历史净值
 */
@Data
public class FundJzhPO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 净值日期
     */
    private Date dt;

    /**
     * 单位净值
     */
    private float unitVal;

    /**
     * 历史净值
     */
    private float totalVal;

    /**
     * 日涨幅
     */
    private float dayGrowth;

}
