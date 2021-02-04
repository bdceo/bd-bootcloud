package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

/**
 * 基金概况
 */
@Data
public class FundInfoPO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 投资目标
     */
    private String target;

    /**
     * 投资理念
     */
    private String idea;

    /**
     * 投资范围
     */
    private String range;

    /**
     * 投资策略
     */
    private String strategy;

    /**
     * 分红政策
     */
    private String bonus;


}
