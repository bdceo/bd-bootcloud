package com.bdsoft.crawler.modules.fund.po;

import lombok.Data;

/**
 * 基金手续费信息
 */
@Data
public class FundFeePO {

    /**
     * 基金标识
     */
    private String code;

    /**
     * 管理费
     */
    private float glf;

    /**
     * 托管费
     */
    private float tgf;

    /**
     * 销售服务费
     */
    private float xsh;

    /**
     * 认购费
     */
    private float rgf;

    /**
     * 申购、赎回费
     */
    private float sgf;
    private float shf;

    public FundFeePO() {
    }

    public FundFeePO(String code) {
        this.code = code;
    }

}
